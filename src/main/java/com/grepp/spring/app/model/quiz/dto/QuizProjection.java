package com.grepp.spring.app.model.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizProjection {

    private Integer week;
    private Long quizId;
    private String question;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
}