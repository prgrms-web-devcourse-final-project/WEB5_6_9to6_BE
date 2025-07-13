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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizSetRepository quizSetRepository;
    private final QuizRepository quizRepository;
    private final ChoiceRepository choiceRepository;
    private final StudyGoalRepository studyGoalRepository;
    private final GenerativeModel generativeModel;

    @Transactional
    public QuizSet createProblemsForWeek(Long studyId, int week) throws IOException {
        quizSetRepository.findByStudyIdAndWeek(studyId, week).ifPresent(qs -> {
            throw new IllegalStateException("이미 해당 주차의 퀴즈가 존재합니다.");
        });

        String topic = getStudyTopicByWeekOrder(studyId, week);

        List<QuizGenerationDto> generatedQuizzes = requestQuizGenerationToLlm(topic, 5);

        QuizSet quizSet = QuizSet.builder()
                .studyId(studyId)
                .week(week)
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

    private String getStudyTopicByWeekOrder(Long studyId, int week) {
        List<StudyGoal> allGoals = studyGoalRepository.findGoalsByStudyId(studyId);

        int targetIndex = week - 1;
        if (allGoals.size() <= targetIndex) {
            throw new EntityNotFoundException(String.format("%d주차에 해당하는 스터디 목표가 없습니다.", week));
        }
        StudyGoal targetGoal = allGoals.get(targetIndex);

        return targetGoal.getContent();
    }

    private List<QuizGenerationDto> requestQuizGenerationToLlm(String topic, int count) throws IOException {
        String prompt = String.format("""
            다음 학습 목표에 대해 초보자가 이해해야 할 핵심 개념을 묻는 4지선다형 객관식 문제 %d개를 만들어줘.
            학습 목표: "%s"
            
            반드시 문제(question), 4개의 선택지(choices), 1부터 시작하는 정답 인덱스(answer) 키를 가진 JSON 객체들의 배열 형식으로만 응답해줘. 다른 설명은 절대 추가하지 마.
            """, count, topic);
        try {
            String jsonResponse = ResponseHandler.getText(generativeModel.generateContent(prompt));
            String cleanJsonResponse = jsonResponse.replace("```json", "").replace("```", "").trim();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(cleanJsonResponse, new TypeReference<>() {});
        } catch (Exception e) {
            System.err.println("Gemini API 호출 중 오류 발생: " + e.getMessage());
            throw new IOException("LLM API와의 통신에 실패했습니다.", e);
        }
    }
}