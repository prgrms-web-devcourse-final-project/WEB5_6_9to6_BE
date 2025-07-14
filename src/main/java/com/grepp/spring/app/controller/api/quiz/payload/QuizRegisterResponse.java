package com.grepp.spring.app.controller.api.quiz.payload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class QuizRegisterResponse {
    private final Long quizSetId;
    private final int registeredQuizCount;
}