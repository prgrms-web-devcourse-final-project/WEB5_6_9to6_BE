package com.grepp.spring.infra.error.exceptions.Quiz;

public class InvalidQuizGradeRequestException extends RuntimeException  {

    public InvalidQuizGradeRequestException(String message) {
        super(message);
    }
}
