package com.grepp.spring.infra.error.exceptions;

public class QuizSetNotFoundException extends RuntimeException  {

    public QuizSetNotFoundException(String message) {
        super(message);
    }
}
