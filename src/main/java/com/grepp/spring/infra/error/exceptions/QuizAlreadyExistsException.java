package com.grepp.spring.infra.error.exceptions;

public class QuizAlreadyExistsException extends RuntimeException  {

    public QuizAlreadyExistsException(String message) {
        super(message);
    }
}