package com.grepp.studium.quiz_set.repos;

import com.grepp.studium.quiz_set.domain.QuizSet;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuizSetRepository extends JpaRepository<QuizSet, Integer> {
}
