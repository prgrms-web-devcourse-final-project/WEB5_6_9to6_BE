package com.grepp.spring.app.controller.api.quiz.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuizGradingResponse {
    private int week;
    private int correctCount;
}