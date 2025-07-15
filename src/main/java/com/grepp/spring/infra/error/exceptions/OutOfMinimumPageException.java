package com.grepp.spring.infra.error.exceptions;

public class OutOfMinimumPageException extends RuntimeException {

    public OutOfMinimumPageException(String message) {
        super(message);
    }
}
