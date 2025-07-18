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
import com.grepp.spring.infra.error.exceptions.Quiz.QuizAlreadyExistsException;
import com.grepp.spring.infra.error.exceptions.Quiz.QuizGenerationFailedException;
import com.grepp.spring.infra.error.exceptions.Quiz.StudyGoalNotFoundException;
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
public class QuizCreateService {

    private final QuizSetRepository quizSetRepository;
    private final QuizRepository quizRepository;
    private final ChoiceRepository choiceRepository;
    private final StudyGoalRepository studyGoalRepository;
    private final GenerativeModel generativeModel;

    @Transactional
    @Async
    public void createNextQuiz(Long studyId) throws IOException {
        int lastWeek = quizSetRepository.findTopByStudyIdOrderByWeekDesc(studyId)
                .map(QuizSet::getWeek)
                .orElse(0);

        int nextWeek = lastWeek + 1;
        createProblems(studyId, nextWeek);
    }

    @Transactional
    public QuizSet createProblems(Long studyId, int week) throws IOException {
        checkQuizDuplication(studyId, week);
        StudyGoal goal = getStudyGoal(studyId, week);
        List<QuizGenerationDto> generatedQuizzes = generateQuizzesFromLLM(goal.getContent(), 5);
        return saveGeneratedQuizzes(studyId, week, goal, generatedQuizzes);
    }

    private void checkQuizDuplication(Long studyId, int week) {
        quizSetRepository.findByStudyIdAndWeek(studyId, week)
                .ifPresent(q -> {
                    throw new QuizAlreadyExistsException("이미 해당 주차에 퀴즈가 존재합니다.");
                });
    }

    private StudyGoal getStudyGoal(Long studyId, int week) {

        List<StudyGoal> goals = studyGoalRepository.findGoalsByStudyId(studyId);
        int index = week - 1;

        if (index >= goals.size()) {
            throw new StudyGoalNotFoundException(week + "주차에 해당하는 스터디 목표가 존재하지 않습니다.");
        }

        return goals.get(index);
    }

    @Retryable(
            value = {IOException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 2000)
    )
    private List<QuizGenerationDto> generateQuizzesFromLLM(String topic, int count) throws IOException {
        String prompt = buildPrompt(topic, count);

        try {
            var response = generativeModel.generateContent(prompt);
            String rawResponse = ResponseHandler.getText(response).trim();

            Matcher matcher = Pattern.compile("\\[\\s*\\{[\\s\\S]*?}\\s*\\]", Pattern.DOTALL).matcher(rawResponse);
            if (matcher.find()) {
                String json = matcher.group();
                return new ObjectMapper().readValue(json, new TypeReference<>() {});
            } else {
                throw new QuizGenerationFailedException("LLM 응답에서 JSON 배열을 찾을 수 없습니다.");
            }

        } catch (Exception e) {
            throw new QuizGenerationFailedException("퀴즈 생성 실패: " + e.getMessage());
        }
    }

    private String buildPrompt(String topic, int count) {
        return String.format("""
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
    }

    private QuizSet saveGeneratedQuizzes(Long studyId, int week, StudyGoal goal, List<QuizGenerationDto> dtos) {

        QuizSet quizSet = QuizSet.builder()
                .studyId(studyId)
                .week(week)
                .studyGoal(goal)
                .activated(true)
                .build();
        quizSetRepository.save(quizSet);

        for (QuizGenerationDto dto : dtos) {
            Quiz quiz = quizRepository.save(Quiz.builder()
                    .quizSet(quizSet)
                    .question(dto.getQuestion())
                    .answer(dto.getAnswer())
                    .activated(true)
                    .build());

            choiceRepository.save(Choice.builder()
                    .quiz(quiz)
                    .choice1(dto.getChoices().get(0))
                    .choice2(dto.getChoices().get(1))
                    .choice3(dto.getChoices().get(2))
                    .choice4(dto.getChoices().get(3))
                    .activated(true)
                    .build());
        }

        return quizSet;
    }
}
