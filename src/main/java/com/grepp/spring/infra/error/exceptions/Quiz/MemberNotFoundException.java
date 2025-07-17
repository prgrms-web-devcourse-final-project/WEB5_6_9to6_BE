package com.grepp.spring.infra.error.exceptions.Quiz;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException(String message) {
        super(message);
    }
}
