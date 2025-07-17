package com.grepp.spring.app.model.quiz.repository;

import com.grepp.spring.app.model.quiz.dto.QuizProjection;
import com.grepp.spring.app.model.quiz.entity.QuizSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface QuizSetRepository extends JpaRepository<QuizSet, Long> {

    // 스터디에 존재하는 퀴즈셋 가져옴
    @Query("""
    SELECT new com.grepp.spring.app.model.quiz.dto.QuizProjection(
        qs.week, q.id, q.question,
        c.choice1, c.choice2, c.choice3, c.choice4,
        q.answer
    )
    FROM QuizSet qs
    JOIN qs.quizzes q
    JOIN q.choice c
    WHERE qs.studyId = :studyId
      AND qs.activated = true
      AND q.activated = true
    ORDER BY qs.week ASC, q.id ASC
    """)
    List<QuizProjection> findQuizSetsByStudyId(@Param("studyId") Long studyId);

    Optional<QuizSet> findByStudyIdAndWeek(Long studyId, int week);

    Optional<QuizSet> findTopByStudyIdOrderByWeekDesc(Long studyId);
}