package com.grepp.spring.app.model.quiz.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import com.grepp.spring.app.model.quiz.dto.QuizGenerationDto;
import com.grepp.spring.app.model.quiz.entity.Choice;
import com.grepp.spring.app.model.quiz.entity.Quiz;
import com.grepp.spring.app.model.quiz.entity.QuizSet;
import com.grepp.spring.app.model.quiz.repository.ChoiceRepository;
import com.grepp.spring.app.model.quiz.repository.QuizRepository;
import com.grepp.spring.app.model.quiz.repository.QuizSetRepository;
import com.grepp.spring.app.model.study.entity.StudyGoal;
import com.grepp.spring.app.model.study.repository.StudyGoalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizSetRepository quizSetRepository;
    private final QuizRepository quizRepository;
    private final ChoiceRepository choiceRepository;
    private final StudyGoalRepository studyGoalRepository;
    private final GenerativeModel generativeModel;

    @Transactional
    @Async
    public void createNextQuizForStudy(Long studyId) throws IOException {
        int lastWeek = quizSetRepository.findTopByStudyIdOrderByWeekDesc(studyId)
                .map(QuizSet::getWeek)
                .orElse(0);

        int nextWeek = lastWeek + 1;
        createProblemsForWeek(studyId, nextWeek);
    }

    @Transactional
    public QuizSet createProblemsForWeek(Long studyId, int week) throws IOException {
        quizSetRepository.findByStudyIdAndWeek(studyId, week).ifPresent(qs -> {
            throw new IllegalStateException("이미 해당 주차의 퀴즈가 존재합니다.");
        });

        StudyGoal targetGoal = getStudyTopicByWeekOrder(studyId, week);
        List<QuizGenerationDto> generatedQuizzes = requestQuizGenerationToLlm(targetGoal.getContent(), 5);

        QuizSet quizSet = QuizSet.builder()
                .studyId(studyId)
                .week(week)
                .studyGoal(targetGoal)
                .activated(true)
                .build();
        quizSetRepository.save(quizSet);

        for (QuizGenerationDto quizDto : generatedQuizzes) {
            Quiz quiz = Quiz.builder()
                    .quizSet(quizSet)
                    .question(quizDto.getQuestion())
                    .answer(quizDto.getAnswer())
                    .activated(true)
                    .build();
            quizRepository.save(quiz);

            Choice choice = Choice.builder()
                    .quiz(quiz)
                    .choice1(quizDto.getChoices().get(0))
                    .choice2(quizDto.getChoices().get(1))
                    .choice3(quizDto.getChoices().get(2))
                    .choice4(quizDto.getChoices().get(3))
                    .activated(true)
                    .build();
            choiceRepository.save(choice);
        }

        return quizSet;
    }

    private StudyGoal getStudyTopicByWeekOrder(Long studyId, int week) {
        int pageIndex = week - 1;
        if (pageIndex < 0) {
            throw new IllegalArgumentException("주차(week)는 1 이상이어야 합니다.");
        }

        List<StudyGoal> goals = studyGoalRepository.findGoalsByStudyId(studyId);
        if (goals.size() <= pageIndex) {
            throw new EntityNotFoundException(week + "주차에 해당하는 스터디 목표가 없습니다.");
        }

        return goals.get(pageIndex);
    }

    @Retryable(
            value = {IOException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    private List<QuizGenerationDto> requestQuizGenerationToLlm(String topic, int count) throws IOException {
        String prompt = String.format("""
    # 역할
    당신은 주어진 주제에 대해, 엄격한 JSON 형식으로만 퀴즈를 생성하는 API입니다.

    # 지시사항
    1.  **주제**: "%s"
    2.  **요청**: 위 주제에 대한 4지선다 퀴즈를 %d개 생성하세요.
    3.  **규칙**:
        - `question`의 값은 반드시 250자 미만이어야 합니다.
        - `answer`의 값은 정답 선택지의 1-based index (1, 2, 3, 4 중 하나)입니다.
    4.  **안전 지침**:
        - 생성하는 모든 내용은 교육적 목적에 부합해야 합니다.
        - 유해하거나 공격적이거나 논란의 소지가 있는 내용은 절대 포함하지 마세요.

    # 출력 형식
    - 출력은 오직 아래 명시된 JSON 배열 형식이어야 합니다.
    - JSON 외부에는 어떠한 설명, 주석, 코드 마크다운(```)도 절대 포함해서는 안 됩니다.
    - 오직 JSON 데이터만 반환하세요.
    
    [
      {
        "question": "생성된 질문",
        "choices": ["선택지 1", "선택지 2", "선택지 3", "선택지 4"],
        "answer": 1
      }
    ]
    """, topic, count);

        try {
            var response = generativeModel.generateContent(prompt);
            String rawResponse = ResponseHandler.getText(response).trim();

            System.out.println("=== LLM Raw Response Start ===");
            System.out.println(rawResponse);
            System.out.println("=== LLM Raw Response End ===");

            Pattern pattern = Pattern.compile("\\[\\s*\\{[\\s\\S]*?}\\s*\\]", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(rawResponse);

            if (matcher.find()) {
                String jsonResponse = matcher.group();
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(jsonResponse, new TypeReference<>() {});
            } else {
                System.err.println("⚠ JSON 배열을 찾을 수 없습니다. 전체 응답:\n" + rawResponse);
                throw new IOException("LLM 응답에서 유효한 JSON 배열을 찾을 수 없습니다.");
            }

        } catch (Exception e) {
            System.err.println("LLM API 호출 또는 JSON 파싱 실패: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("LLM API와의 통신 또는 응답 처리 중 오류가 발생했습니다.", e);
        }
    }
}
