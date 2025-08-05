package com.grepp.spring.app.model.quiz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuizGradingResponse {
    private int week;
    private int correctCount;
}