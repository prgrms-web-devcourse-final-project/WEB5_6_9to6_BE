package com.grepp.spring.app.model.quiz.repository.quizRepository;

import com.grepp.spring.app.model.quiz.entity.Quiz;
import java.util.List;

public interface QuizRepositoryCustom {
    List<Quiz> findQuizzesByStudyIdAndWeek(Long studyId, int week);
}