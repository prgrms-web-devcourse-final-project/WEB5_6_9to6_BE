package com.grepp.spring.app.model.quiz.repository;

import com.grepp.spring.app.model.quiz.entity.Choice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChoiceRepository extends JpaRepository<Choice, Long> {}