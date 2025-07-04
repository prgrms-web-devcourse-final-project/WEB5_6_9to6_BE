package com.grepp.studium.quiz.repos;

import com.grepp.studium.quiz.domain.Quiz;
import com.grepp.studium.quiz_set.domain.QuizSet;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuizRepository extends JpaRepository<Quiz, Integer> {

    Quiz findFirstByQuizSet(QuizSet quizSet);

}
