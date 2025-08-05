package com.grepp.spring.app.model.quiz.service;

import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import com.grepp.spring.app.model.quiz.entity.Quiz;
import com.grepp.spring.app.model.quiz.entity.QuizSet;
import com.grepp.spring.app.model.quiz.repository.ChoiceRepository;
import com.grepp.spring.app.model.quiz.repository.quizRepository.QuizRepository;
import com.grepp.spring.app.model.quiz.repository.quizSetRepository.QuizSetRepository;
import com.grepp.spring.app.model.study.entity.Study;
import com.grepp.spring.app.model.study.entity.StudyGoal;
import com.grepp.spring.app.model.study.repository.goal.StudyGoalRepository;
import com.grepp.spring.infra.error.exceptions.Quiz.QuizAlreadyExistsException;
import com.grepp.spring.infra.error.exceptions.Quiz.QuizGenerationFailedException;
import com.grepp.spring.infra.error.exceptions.Quiz.StudyGoalNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizCreateServiceTest {

    @InjectMocks
    private QuizCreateService quizCreateService;

    @Mock private QuizSetRepository quizSetRepository;
    @Mock private QuizRepository quizRepository;
    @Mock private ChoiceRepository choiceRepository;
    @Mock private StudyGoalRepository studyGoalRepository;
    @Mock private GenerativeModel generativeModel;

    @Test
    @DisplayName("성공: 새로운 퀴즈를 생성하고 저장한다")
    void createProblems_Success() throws IOException {
        // given
        Long studyId = 1L;
        int week = 1;

        StudyGoal realGoal = StudyGoal.builder().content("자바 기초").study(mock(Study.class)).build();
        String llmJsonResponse = """
                [
                  { "question": "제일 맛있는 과일은??", "choices": ["파인애플", "그냥애플", "워터멜론", "그냥멜론"], "answer": 1 }
                ]
                """;

        when(quizSetRepository.findByStudyIdAndWeek(studyId, week)).thenReturn(Optional.empty());
        when(studyGoalRepository.findGoalsByStudyId(studyId)).thenReturn(List.of(realGoal));

        GenerateContentResponse mockResponse = mock(GenerateContentResponse.class);
        when(generativeModel.generateContent(anyString())).thenReturn(mockResponse);

        when(quizRepository.save(any(Quiz.class))).thenReturn(mock(Quiz.class));

        try (MockedStatic<ResponseHandler> mockedHandler = mockStatic(ResponseHandler.class)) {
            mockedHandler.when(() -> ResponseHandler.getText(mockResponse)).thenReturn(llmJsonResponse);

            // when
            quizCreateService.createProblems(studyId, week);

            // then
            verify(quizSetRepository, times(1)).save(any(QuizSet.class));
            verify(quizRepository, times(1)).save(any());
            verify(choiceRepository, times(1)).save(any());
        }
    }

    @Test
    @DisplayName("실패: 이미 해당 주차에 퀴즈가 존재하면 QuizAlreadyExistsException 발생")
    void createProblems_Fail_QuizAlreadyExists() {
        // given
        // new QuizSet()은 접근제한으로 호출 불가하므로, mock()으로 가짜 객체를 생성
        when(quizSetRepository.findByStudyIdAndWeek(1L, 1)).thenReturn(Optional.of(mock(QuizSet.class)));

        // when & then
        assertThrows(QuizAlreadyExistsException.class, () -> quizCreateService.createProblems(1L, 1));
    }

    @Test
    @DisplayName("실패: 해당 주차의 스터디 목표가 없으면 StudyGoalNotFoundException 발생")
    void createProblems_Fail_StudyGoalNotFound() {
        when(quizSetRepository.findByStudyIdAndWeek(1L, 2)).thenReturn(Optional.empty());
        when(studyGoalRepository.findGoalsByStudyId(1L)).thenReturn(List.of(mock(StudyGoal.class)));
        assertThrows(StudyGoalNotFoundException.class, () -> quizCreateService.createProblems(1L, 2));
    }

    @Test
    @DisplayName("실패: LLM API 호출이 실패하면 QuizGenerationFailedException 발생")
    void createProblems_Fail_LLM_IOException() throws IOException {
        when(quizSetRepository.findByStudyIdAndWeek(anyLong(), anyInt())).thenReturn(Optional.empty());
        when(studyGoalRepository.findGoalsByStudyId(anyLong())).thenReturn(List.of(mock(StudyGoal.class)));
        when(generativeModel.generateContent(anyString())).thenThrow(new IOException("API 통신 오류"));
        assertThrows(QuizGenerationFailedException.class, () -> quizCreateService.createProblems(1L, 1));
    }
}