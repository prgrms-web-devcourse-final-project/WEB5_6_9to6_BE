package com.grepp.spring.app.model.quiz.service;

import com.grepp.spring.app.controller.api.quiz.payload.SurvivalResultRequest;
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

    @Transactional
    public void registerSurvivalResult(Long studyId, int week, SurvivalResultRequest request) {

        Long studyMemberId = request.getStudyMemberId();
        studyMemberRepository.findById(studyMemberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디 멤버 ID입니다: " + studyMemberId));


        QuizSet quizSet = quizSetRepository.findByStudyIdAndWeek(studyId, week)
                .orElseThrow(() -> new IllegalArgumentException("해당 주차의 퀴즈 세트가 존재하지 않습니다."));

        QuizRecord quizRecord = QuizRecord.builder()
                .studyMemberId(studyMemberId)
                .quizSet(quizSet)
                .isSurvived(request.isSurvived())
                .activated(true)
                .build();

        quizRecordRepository.save(quizRecord);
    }
}