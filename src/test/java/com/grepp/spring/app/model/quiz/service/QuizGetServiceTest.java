package com.grepp.spring.app.model.quiz.service;

import com.grepp.spring.app.model.quiz.dto.response.QuizListResponse;
import com.grepp.spring.app.model.quiz.dto.internal.QuizProjection;
import com.grepp.spring.app.model.quiz.repository.quizSetRepository.QuizSetRepository;
import com.grepp.spring.app.model.study.repository.StudyRepository;
import com.grepp.spring.infra.error.exceptions.Quiz.InvalidQuizException;
import com.grepp.spring.infra.error.exceptions.Quiz.MemberNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizGetServiceTest {

    @InjectMocks
    private QuizGetService quizGetService;

    @Mock
    private QuizSetRepository quizSetRepository;

    @Mock
    private StudyRepository studyRepository;

    @Test
    @DisplayName("성공: 퀴즈 목록 조회 시 주차별로 그룹화 및 정렬하여 반환한다")
    void getQuizzes_Success() {
        
        // given: Quiz 등록
        Long studyId = 1L;
        List<QuizProjection> projections = Arrays.asList(
                new QuizProjection(2, 102L, "2주차 질문", "A", "B", "C", "D", 1),
                new QuizProjection(1, 101L, "1주차 질문", "A", "B", "C", "D", 2),
                new QuizProjection(1, 103L, "1주차 두번째 질문", "E", "F", "G", "H", 4)
        );

        when(studyRepository.existsById(studyId)).thenReturn(true);
        when(quizSetRepository.findQuizSetsByStudyId(studyId)).thenReturn(projections);

        // when: 서비스 메소드 호출
        List<QuizListResponse> result = quizGetService.getQuizzes(studyId);

        // then: 결과 검증
        assertThat(result.size()).isEqualTo(2);

        QuizListResponse week1Response = result.get(0);
        assertThat(week1Response.getWeek()).isEqualTo(1);
        assertThat(week1Response.getQuizzes().size()).isEqualTo(2);
        assertThat(week1Response.getQuizzes().get(1).getQuestion()).isEqualTo("1주차 두번째 질문");

        QuizListResponse week2Response = result.get(1);
        assertThat(week2Response.getWeek()).isEqualTo(2);
        assertThat(week2Response.getQuizzes().size()).isEqualTo(1);
        assertThat(week2Response.getQuizzes().get(0).getQuestion()).isEqualTo("2주차 질문");
    }

    @Test
    @DisplayName("실패: 존재하지 않는 스터디 ID로 조회 시 MemberNotFoundException 발생")
    void getQuizzes_Fail1() {
        // given: 존재하지 않는 스터디
        Long nonExistentStudyId = 999L;
        when(studyRepository.existsById(nonExistentStudyId)).thenReturn(false);

        // when & then: 지정된 예외가 발생하는지 검증
        assertThrows(MemberNotFoundException.class, () -> {
            quizGetService.getQuizzes(nonExistentStudyId);
        });
    }

    @Test
    @DisplayName("실패: 스터디에 생성된 퀴즈가 없을 때 InvalidQuizException 발생")
    void getQuizzes_Fail2() {
        // given: 스터디는 존재하지만 스터티 내 퀴즈는 없음
        Long studyId = 1L;
        when(studyRepository.existsById(studyId)).thenReturn(true);
        when(quizSetRepository.findQuizSetsByStudyId(studyId)).thenReturn(Collections.emptyList());

        // when & then: 지정된 예외가 발생하는지 검증
        assertThrows(InvalidQuizException.class, () -> {
            quizGetService.getQuizzes(studyId);
        });
    }
}