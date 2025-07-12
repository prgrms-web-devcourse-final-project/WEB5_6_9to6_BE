package com.grepp.spring.app.model.quiz.repository;

import com.grepp.spring.app.model.quiz.entity.QuizSetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface QuizSetRepository extends JpaRepository<QuizSetEntity, Long> {

    // 스터디에 존재하는 퀴즈셋 가져옴
    @Query("SELECT DISTINCT qs FROM QuizSetEntity qs " +
            "LEFT JOIN FETCH qs.quizzes q " +
            "LEFT JOIN FETCH q.choice c " +
            "WHERE qs.studyId = :studyId AND qs.activated = true AND q.activated = true " +
            "ORDER BY qs.week ASC")
    List<QuizSetEntity> findQuizSetsByStudyId(@Param("studyId") Long studyId);
}