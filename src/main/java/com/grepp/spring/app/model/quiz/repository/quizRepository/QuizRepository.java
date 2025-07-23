package com.grepp.spring.app.model.quiz.repository.quizRepository;

import com.grepp.spring.app.model.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long>, QuizRepositoryCustom {

}