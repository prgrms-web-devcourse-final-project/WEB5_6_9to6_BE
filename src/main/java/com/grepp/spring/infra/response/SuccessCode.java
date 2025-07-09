package com.grepp.spring.infra.response;

import org.springframework.http.HttpStatus;

public enum SuccessCode {
    SUCCESS("0000", HttpStatus.OK, "정상적으로 완료되었습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;

    SuccessCode(String code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public String code() {
        return code;
    }

    public HttpStatus status() {
        return status;
    }

    public String message() {
        return message;
    }
}
