package com.grepp.studium.choice.repos;

import com.grepp.studium.choice.domain.Choice;
import com.grepp.studium.quiz.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChoiceRepository extends JpaRepository<Choice, Integer> {

    Choice findFirstByQuiz(Quiz quiz);

}
