package com.grepp.spring.app.model.quiz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuizRegisterResponse {
    private Long quizSetId;
    private int registeredQuizCount;
}