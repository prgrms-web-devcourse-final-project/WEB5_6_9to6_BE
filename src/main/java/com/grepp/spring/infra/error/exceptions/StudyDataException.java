package com.grepp.spring.infra.error.exceptions;

public class StudyDataException extends RuntimeException {

    public StudyDataException(String message) {
        super(message);
    }

    public StudyDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
