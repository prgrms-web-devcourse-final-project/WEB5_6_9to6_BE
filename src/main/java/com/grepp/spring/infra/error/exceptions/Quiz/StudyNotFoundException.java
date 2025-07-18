package com.grepp.spring.infra.error.exceptions.Quiz;

public class StudyNotFoundException extends RuntimeException {

    public StudyNotFoundException(String message) {
        super(message);
    }
}
