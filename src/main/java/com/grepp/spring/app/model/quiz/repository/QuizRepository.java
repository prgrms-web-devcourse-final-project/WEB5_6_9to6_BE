package com.grepp.spring.app.model.quiz.repository;

import com.grepp.spring.app.model.quiz.entity.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface QuizRepository extends JpaRepository<QuizEntity, Long> {
    @Query("SELECT q FROM QuizEntity q WHERE q.quizSet.week = :week")
    List<QuizEntity> findQuizzesByWeek(@Param("week") int week);
}
