package com.grepp.spring.app.model.quiz.repository;

import com.grepp.spring.app.model.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    @Query("SELECT q FROM Quiz q WHERE q.quizSet.week = :week")
    List<Quiz> findQuizzesByWeek(@Param("week") int week);
}
