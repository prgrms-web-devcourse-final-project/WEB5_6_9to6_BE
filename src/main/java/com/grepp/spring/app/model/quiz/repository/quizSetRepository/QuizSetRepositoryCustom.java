package com.grepp.spring.app.model.quiz.repository.quizSetRepository;

import com.grepp.spring.app.model.quiz.dto.internal.QuizProjection;
import java.util.List;

public interface QuizSetRepositoryCustom {
    List<QuizProjection> findQuizSetsByStudyId(Long studyId);
}