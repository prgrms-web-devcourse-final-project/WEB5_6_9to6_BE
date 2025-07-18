package com.grepp.spring.infra.error.exceptions.Quiz;

public class StudyMemberNotFoundException extends RuntimeException {

    public StudyMemberNotFoundException(String message) {
        super(message);
    }
}
