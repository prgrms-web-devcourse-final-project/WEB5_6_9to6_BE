package com.grepp.spring.app.model.quiz.service;

import com.grepp.spring.app.controller.api.quiz.payload.SurvivalResultRequest;
import com.grepp.spring.app.model.member.entity.StudyMember;
import com.grepp.spring.app.model.member.repository.MemberRepository;
import com.grepp.spring.app.model.member.repository.StudyMemberRepository;
import com.grepp.spring.app.model.quiz.entity.QuizRecord;
import com.grepp.spring.app.model.quiz.entity.QuizSet;
import com.grepp.spring.app.model.quiz.repository.QuizRecordRepository;
import com.grepp.spring.app.model.quiz.repository.QuizSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SurvivalResultService {

    private final StudyMemberRepository studyMemberRepository;
    private final QuizSetRepository quizSetRepository;
    private final QuizRecordRepository quizRecordRepository;
    private final MemberRepository memberRepository;

    // 생존 시 지급할 리워드 포인트를 상수로 정의합니다.
    private static final int SURVIVAL_REWARD_POINTS = 10;

    @Transactional
    public void processSurvivalResult(Long studyId, int week, SurvivalResultRequest request) {

        Long studyMemberId = request.getStudyMemberId();
        StudyMember studyMember = studyMemberRepository.findByStudy_StudyIdAndStudyMemberId(studyId, studyMemberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 스터디에 존재하지 않는 스터디 멤버 ID입니다: " + studyMemberId));

        QuizSet quizSet = quizSetRepository.findByStudyIdAndWeek(studyId, week)
                .orElseThrow(() -> new IllegalArgumentException("해당 주차의 퀴즈 세트가 존재하지 않습니다."));

        if (request.isSurvived()) {
            Long memberId = studyMember.getMember().getId();
            memberRepository.addRewardPoints(memberId, SURVIVAL_REWARD_POINTS);
        } else {
            studyMember.unActivated();
        }

        QuizRecord quizRecord = QuizRecord.builder()
                .studyMemberId(studyMemberId)
                .quizSet(quizSet)
                .isSurvived(request.isSurvived())
                .activated(true)
                .build();

        quizRecordRepository.save(quizRecord);
    }
}