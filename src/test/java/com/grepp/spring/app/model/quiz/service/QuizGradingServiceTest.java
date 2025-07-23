package com.grepp.spring.app.model.quiz.service;

import com.grepp.spring.app.controller.api.quiz.payload.QuizGradingRequest;
import com.grepp.spring.app.controller.api.quiz.payload.QuizGradingResponse;
import com.grepp.spring.app.model.quiz.entity.Quiz;
import com.grepp.spring.app.model.quiz.repository.QuizRepository;
import com.grepp.spring.infra.error.exceptions.Quiz.InvalidQuizGradeRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuizGradingServiceTest {

    @InjectMocks
    private QuizGradingService quizGradingService;

    @Mock
    private QuizRepository quizRepository;

    @Test
    @DisplayName("성공: 퀴즈 채점 시 정답 개수를 정확히 계산하여 반환")
    void grade_Success() {
        // given: 정상적인 로직 답안지 5개 실제 퀴즈 5개
        QuizGradingRequest request = new QuizGradingRequest(1L, 1L, 1, Arrays.asList(1, 2, 4, 4, 4));

        List<Quiz> quizzesFrom = Arrays.asList(
                Quiz.builder().answer(1).build(),
                Quiz.builder().answer(2).build(),
                Quiz.builder().answer(4).build(),
                Quiz.builder().answer(4).build(),
                Quiz.builder().answer(4).build()
        );

        when(quizRepository.findQuizzesByStudyIdAndWeek(1L, 1)).thenReturn(quizzesFrom);

        // when: 서비스 메소드 호출
        QuizGradingResponse response = quizGradingService.grade(request);

        // then: 결과 검증
        assertThat(response.getCorrectCount()).isEqualTo(5);
    }

    @Test
    @DisplayName("실패: 답안지 개수와 퀴즈 개수가 다르면 InvalidQuizGradeRequestException 발생")
    void grade_Fail_MismatchAnswerCount() {
        // given: 답안지는 4개 제출
        QuizGradingRequest request = new QuizGradingRequest(1L, 1L, 1, Arrays.asList(1, 2, 3, 4));

        // 실제 퀴즈는 5개
        List<Quiz> quizzesFromDb = Arrays.asList(
                Quiz.builder().build(),
                Quiz.builder().build(),
                Quiz.builder().build()
        );

        when(quizRepository.findQuizzesByStudyIdAndWeek(1L, 1)).thenReturn(quizzesFromDb);

        // when & then: 예외가 발생하는지 검증
        assertThrows(InvalidQuizGradeRequestException.class, () -> {
            quizGradingService.grade(request);
        });
    }
}