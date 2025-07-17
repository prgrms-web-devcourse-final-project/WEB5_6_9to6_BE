package com.grepp.spring.infra.error.exceptions.Quiz;

public class StudyGoalNotFoundException extends RuntimeException  {

    public StudyGoalNotFoundException(String message) {
        super(message);
    }
}