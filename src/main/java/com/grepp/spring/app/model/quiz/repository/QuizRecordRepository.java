package com.grepp.spring.app.model.quiz.repository;

import com.grepp.spring.app.model.quiz.entity.QuizRecord;
import com.grepp.spring.app.model.quiz.entity.QuizSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRecordRepository extends JpaRepository<QuizRecord, Long> {

    boolean existsByStudyMemberIdAndQuizSet(Long studyMemberId, QuizSet quizSet);

    List<QuizRecord> findAllByQuizSet(QuizSet quizSet);
}