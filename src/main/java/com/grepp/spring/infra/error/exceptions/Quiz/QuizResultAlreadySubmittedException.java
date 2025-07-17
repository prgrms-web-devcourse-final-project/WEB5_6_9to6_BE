package com.grepp.spring.infra.error.exceptions.Quiz;

public class QuizResultAlreadySubmittedException extends RuntimeException   {

    public QuizResultAlreadySubmittedException(String message) {
        super(message);
    }
}
