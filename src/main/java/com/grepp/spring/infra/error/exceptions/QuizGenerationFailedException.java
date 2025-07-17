package com.grepp.spring.infra.error.exceptions;

public class QuizGenerationFailedException extends RuntimeException {

    public QuizGenerationFailedException(String message) {
        super(message);
    }
}