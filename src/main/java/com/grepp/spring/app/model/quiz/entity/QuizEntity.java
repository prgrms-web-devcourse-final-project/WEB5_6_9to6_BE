package com.grepp.spring.app.model.quiz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quiz")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long quizId;

    @Column(nullable = false)
    private long quizSetId;

    @Column(nullable = true)
    private String question;

    @Column(nullable = true)
    private int answer;

    @Column(nullable = false)
    private boolean activated;

}