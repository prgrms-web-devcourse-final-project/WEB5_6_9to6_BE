package com.grepp.spring.infra.error.exceptions;

public class StudyGoalNotFoundException extends RuntimeException  {

    public StudyGoalNotFoundException(String message) {
        super(message);
    }
}