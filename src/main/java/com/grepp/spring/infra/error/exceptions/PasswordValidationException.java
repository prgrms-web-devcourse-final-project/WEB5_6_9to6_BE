package com.grepp.spring.infra.error.exceptions;

import com.grepp.spring.infra.response.ResponseCode;

public class PasswordValidationException extends RuntimeException {

    private final ResponseCode code;

    public PasswordValidationException(ResponseCode code) {
        super(code.message());
        this.code = code;
    }

    public ResponseCode getCode() {
        return code;
    }
}