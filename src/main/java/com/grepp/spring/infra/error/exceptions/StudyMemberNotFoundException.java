package com.grepp.spring.infra.error.exceptions;

public class StudyMemberNotFoundException extends RuntimeException {

    public StudyMemberNotFoundException(String message) {
        super(message);
    }
}
