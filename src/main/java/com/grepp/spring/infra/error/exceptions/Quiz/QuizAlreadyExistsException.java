package com.grepp.spring.infra.error.exceptions.Quiz;

public class QuizAlreadyExistsException extends RuntimeException  {

    public QuizAlreadyExistsException(String message) {
        super(message);
    }
}