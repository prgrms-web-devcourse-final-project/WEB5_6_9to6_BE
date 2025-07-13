package com.grepp.spring.app.controller.api.quiz.payload;

import lombok.Getter;

@Getter
public class QuizRegisterResponse {
    private final String code = "0000";
    private final String message = "서바이벌 문제를 등록했습니다.";
    private final RegisteredQuizData data;

    public QuizRegisterResponse(Long quizSetId, int registeredQuizCount) {
        this.data = new RegisteredQuizData(quizSetId, registeredQuizCount);
    }

    @Getter
    private static class RegisteredQuizData {
        private final Long quizSetId;
        private final int registeredQuizCount;

        public RegisteredQuizData(Long quizSetId, int registeredQuizCount) {
            this.quizSetId = quizSetId;
            this.registeredQuizCount = registeredQuizCount;
        }
    }
}