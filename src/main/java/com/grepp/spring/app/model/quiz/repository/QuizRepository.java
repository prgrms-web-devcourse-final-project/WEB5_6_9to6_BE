package com.grepp.spring.app.model.quiz.repository;

import com.grepp.spring.app.model.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    @Query("""
    SELECT q FROM Quiz q
    WHERE q.quizSet.week = :week
      AND q.quizSet.studyId = :studyId
      AND q.activated = true
    ORDER BY q.id ASC
""")
    List<Quiz> findQuizzesByStudyIdAndWeek(@Param("studyId") Long studyId, @Param("week") int week);
}
