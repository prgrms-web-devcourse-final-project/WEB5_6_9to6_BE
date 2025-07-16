package com.grepp.spring.infra.error.exceptions;

public class OutOfMinimumPageSizeException extends RuntimeException {

    public OutOfMinimumPageSizeException(String message) {
        super(message);
    }
}
