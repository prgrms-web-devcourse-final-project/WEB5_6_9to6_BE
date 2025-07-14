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
            value = {IOException.class}, // IOException이 발생했을 때 재시도
            maxAttempts = 3,             // 최대 3번 시도 (첫 시도 포함)
            backoff = @Backoff(delay = 2000) // 2초(2000ms)의 딜레이를 두고 재시도
    )
    private List<QuizGenerationDto> requestQuizGenerationToLlm(String topic, int count) throws IOException {
        String prompt = String.format(
                "You are a quiz generator for language learners. Create %d multiple-choice questions about the topic '%s'. " +
                        "Follow these rules strictly:\n" +
                        "1. The response MUST be a JSON array format.\n" +
                        "2. Each JSON object in the array must contain three fields: 'question' (string), 'choices' (an array of 4 strings), and 'answer' (integer, 1-4 representing the correct choice index + 1).\n" +
                        "3. The questions must be in English and suitable for learners.\n" +
                        "4. Do not include any text, explanation, or markdown syntax like ```json before or after the JSON array.\n" +
                        "5. Ensure the JSON is perfectly formatted and valid.",
                count, topic
        );

        try {
            var response = generativeModel.generateContent(prompt);
            String rawResponse = ResponseHandler.getText(response).trim();

            Pattern pattern = Pattern.compile("\\[\\s*\\{.*\\}\\s*\\]", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(rawResponse);

            if (matcher.find()) {
                String jsonResponse = matcher.group();
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(jsonResponse, new TypeReference<>() {});
            } else {
                throw new IOException("LLM 응답에서 유효한 JSON 배열을 찾을 수 없습니다.");
            }

        } catch (Exception e) {
            System.err.println("LLM API 호출 또는 JSON 파싱 실패: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("LLM API와의 통신 또는 응답 처리 중 오류가 발생했습니다.", e);
        }
    }
}