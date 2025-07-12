package com.grepp.spring.infra.error.exceptions;

public class AlreadyCheckedAttendanceException extends RuntimeException {
    public AlreadyCheckedAttendanceException(String message) {
        super(message);
    }
}
