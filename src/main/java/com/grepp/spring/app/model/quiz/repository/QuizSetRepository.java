package com.grepp.spring.app.model.quiz.repository;

import com.grepp.spring.app.model.quiz.entity.QuizSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface QuizSetRepository extends JpaRepository<QuizSet, Long> {

    // 스터디에 존재하는 퀴즈셋 가져옴
    @Query("SELECT DISTINCT qs FROM QuizSet qs " +
            "LEFT JOIN FETCH qs.quizzes q " +
            "LEFT JOIN FETCH q.choice c " +
            "WHERE qs.studyId = :studyId AND qs.activated = true AND q.activated = true " +
            "ORDER BY qs.week ASC")
    List<QuizSet> findQuizSetsByStudyId(@Param("studyId") Long studyId);

    Optional<QuizSet> findByStudyIdAndWeek(Long studyId, int week);

    Optional<QuizSet> findTopByStudyIdOrderByWeekDesc(Long studyId);
}