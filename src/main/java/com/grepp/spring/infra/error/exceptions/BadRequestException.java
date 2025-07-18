package com.grepp.spring.infra.error.exceptions;


import com.grepp.spring.infra.response.ResponseCode;

public class BadRequestException extends RuntimeException {

    private final ResponseCode code;

    public BadRequestException(String message) {
        super(message);
        this.code = ResponseCode.BAD_REQUEST;
    }

    public BadRequestException(ResponseCode code) {
        super(code.message());
        this.code = code;
    }

    public ResponseCode getCode() {
        return code;
    }
}
