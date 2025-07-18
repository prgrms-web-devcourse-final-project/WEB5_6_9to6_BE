package com.grepp.spring.app.model.quiz.repository;

import com.grepp.spring.app.model.quiz.entity.QuizRecord;
import com.grepp.spring.app.model.quiz.entity.QuizSet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRecordRepository extends JpaRepository<QuizRecord, Long> {

    boolean existsByStudyMemberIdAndQuizSet_IdAndQuizSet_Week(Long memberId, Long studyId, int week);

    boolean existsByStudyMemberIdAndQuizSet(Long studyMemberId, QuizSet quizSet);
}