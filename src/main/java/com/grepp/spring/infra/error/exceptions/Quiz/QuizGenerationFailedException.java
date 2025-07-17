package com.grepp.spring.infra.error.exceptions.Quiz;

public class QuizGenerationFailedException extends RuntimeException {

    public QuizGenerationFailedException(String message) {
        super(message);
    }
}