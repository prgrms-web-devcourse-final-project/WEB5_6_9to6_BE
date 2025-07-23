package com.grepp.spring.app.model.quiz.repository.quizSetRepository;

import com.grepp.spring.app.model.quiz.entity.QuizSet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface QuizSetRepository extends JpaRepository<QuizSet, Long>, QuizSetRepositoryCustom {


    Optional<QuizSet> findByStudyIdAndWeek(Long studyId, int week);

    Optional<QuizSet> findTopByStudyIdOrderByWeekDesc(Long studyId);
}