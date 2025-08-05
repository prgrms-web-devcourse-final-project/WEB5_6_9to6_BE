package com.grepp.spring.app.model.quiz.service;

import com.grepp.spring.app.controller.api.quiz.payload.SurvivalResultRequest;
import com.grepp.spring.app.model.member.entity.Member;
import com.grepp.spring.app.model.study.entity.StudyMember;
import com.grepp.spring.app.model.member.repository.MemberRepository;
import com.grepp.spring.app.model.member.repository.StudyMemberRepository;
import com.grepp.spring.app.model.quiz.entity.QuizRecord;
import com.grepp.spring.app.model.quiz.entity.QuizSet;
import com.grepp.spring.app.model.quiz.repository.QuizRecordRepository;
import com.grepp.spring.app.model.quiz.repository.QuizSetRepository;
import com.grepp.spring.infra.error.exceptions.Quiz.QuizResultAlreadySubmittedException;
import com.grepp.spring.infra.error.exceptions.Quiz.QuizSetNotFoundException;
import com.grepp.spring.infra.error.exceptions.Quiz.StudyMemberNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SurvivalResultServiceTest {

    @InjectMocks
    private SurvivalResultService survivalResultService;

    @Mock private StudyMemberRepository studyMemberRepository;
    @Mock private QuizSetRepository quizSetRepository;
    @Mock private QuizRecordRepository quizRecordRepository;
    @Mock private MemberRepository memberRepository;

    @Test
    @DisplayName("성공: 서바이벌 생존 시 포인트를 지급하고 결과를 저장")
    void processSurvivalResult_Success_Survived() {
        // given
        Long studyId = 1L;
        int week = 1;
        Long studyMemberId = 10L;
        Long memberId = 100L;
        SurvivalResultRequest request = new SurvivalResultRequest(studyMemberId, true);

        Member mockMember = mock(Member.class);
        StudyMember mockStudyMember = mock(StudyMember.class);
        QuizSet mockQuizSet = mock(QuizSet.class);

        when(mockStudyMember.getMember()).thenReturn(mockMember);
        when(mockMember.getId()).thenReturn(memberId);

        when(studyMemberRepository.findByStudy_StudyIdAndStudyMemberId(studyId, studyMemberId))
                .thenReturn(Optional.of(mockStudyMember));
        when(quizSetRepository.findByStudyIdAndWeek(studyId, week))
                .thenReturn(Optional.of(mockQuizSet));
        when(quizRecordRepository.existsByStudyMemberIdAndQuizSet(studyMemberId, mockQuizSet))
                .thenReturn(false);

        // when
        survivalResultService.processSurvivalResult(studyId, week, request);

        // then
        // memberRepository의 addRewardPoints가 정확한 값으로 1번 호출되었는지 검증
        verify(memberRepository, times(1)).addRewardPoints(memberId, 10);
        // studyMember의 unActivated는 호출되지 않았는지 검증
        verify(mockStudyMember, never()).unActivated();

        // save 메소드로 전달된 QuizRecord 객체를 캡처하여 isSurvived 값이 true인지 검증
        ArgumentCaptor<QuizRecord> captor = ArgumentCaptor.forClass(QuizRecord.class);
        verify(quizRecordRepository).save(captor.capture());
        assertThat(captor.getValue().isSurvived()).isTrue();
    }

    @Test
    @DisplayName("성공: 서바이벌 탈락 시, 스터디 멤버를 비활성화하고 결과를 저장")
    void processSurvivalResult_Success_NotSurvived() {
        // given
        Long studyId = 1L;
        int week = 1;
        Long studyMemberId = 10L;
        SurvivalResultRequest request = new SurvivalResultRequest(studyMemberId, false);

        StudyMember mockStudyMember = mock(StudyMember.class);
        QuizSet mockQuizSet = mock(QuizSet.class);

        when(studyMemberRepository.findByStudy_StudyIdAndStudyMemberId(studyId, studyMemberId))
                .thenReturn(Optional.of(mockStudyMember));
        when(quizSetRepository.findByStudyIdAndWeek(studyId, week))
                .thenReturn(Optional.of(mockQuizSet));
        when(quizRecordRepository.existsByStudyMemberIdAndQuizSet(studyMemberId, mockQuizSet))
                .thenReturn(false);

        // when
        survivalResultService.processSurvivalResult(studyId, week, request);

        // then
        // addRewardPoints는 호출되지 않아야 함
        verify(memberRepository, never()).addRewardPoints(anyLong(), anyInt());
        // unActivated가 1번 호출되었는지 검증
        verify(mockStudyMember, times(1)).unActivated();

        // save 메소드로 전달된 QuizRecord 객체를 캡처하여 isSurvived 값이 false인지 검증
        ArgumentCaptor<QuizRecord> captor = ArgumentCaptor.forClass(QuizRecord.class);
        verify(quizRecordRepository).save(captor.capture());
        assertThat(captor.getValue().isSurvived()).isFalse();
    }

    @Test
    @DisplayName("실패: 스터디 멤버가 존재하지 않으면 StudyMemberNotFoundException 발생")
    void processSurvivalResult_Fail_StudyMemberNotFound() {
        // given
        when(studyMemberRepository.findByStudy_StudyIdAndStudyMemberId(anyLong(), anyLong()))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(StudyMemberNotFoundException.class, () -> {
            survivalResultService.processSurvivalResult(1L, 1, new SurvivalResultRequest(10L, true));
        });
    }

    @Test
    @DisplayName("실패: 퀴즈 세트가 존재하지 않으면 QuizSetNotFoundException 발생")
    void processSurvivalResult_Fail_QuizSetNotFound() {
        // given
        when(studyMemberRepository.findByStudy_StudyIdAndStudyMemberId(anyLong(), anyLong()))
                .thenReturn(Optional.of(mock(StudyMember.class)));
        when(quizSetRepository.findByStudyIdAndWeek(anyLong(), anyInt()))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(QuizSetNotFoundException.class, () -> {
            survivalResultService.processSurvivalResult(1L, 1, new SurvivalResultRequest(10L, true));
        });
    }

    @Test
    @DisplayName("실패: 이미 결과가 제출되었으면 QuizResultAlreadySubmittedException 발생")
    void processSurvivalResult_Fail_AlreadySubmitted() {
        // given
        when(studyMemberRepository.findByStudy_StudyIdAndStudyMemberId(anyLong(), anyLong()))
                .thenReturn(Optional.of(mock(StudyMember.class)));
        when(quizSetRepository.findByStudyIdAndWeek(anyLong(), anyInt()))
                .thenReturn(Optional.of(mock(QuizSet.class)));
        when(quizRecordRepository.existsByStudyMemberIdAndQuizSet(anyLong(), any(QuizSet.class)))
                .thenReturn(true);

        // when & then
        assertThrows(QuizResultAlreadySubmittedException.class, () -> {
            survivalResultService.processSurvivalResult(1L, 1, new SurvivalResultRequest(10L, true));
        });
    }
}