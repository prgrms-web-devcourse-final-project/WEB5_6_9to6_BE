package com.grepp.spring.app.controller.api.quiz.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuizRegisterResponse {
    private Long quizSetId;
    private int registeredQuizCount;
}