package com.grepp.spring.infra.error.exceptions;

public class QuizResultAlreadySubmittedException extends RuntimeException   {

    public QuizResultAlreadySubmittedException(String message) {
        super(message);
    }
}
