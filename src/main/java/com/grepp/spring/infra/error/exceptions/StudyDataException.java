package com.grepp.spring.infra.error.exceptions;

import com.grepp.spring.infra.response.ResponseCode;

public class StudyDataException extends RuntimeException {

    private final ResponseCode code;

    public StudyDataException(ResponseCode code) {
        super(code.message());
        this.code = code;
    }

    public StudyDataException(ResponseCode code, String message) {
        super(message);
        this.code = code;
    }

    public StudyDataException(ResponseCode code, Throwable cause) {
        super(code.message(), cause);
        this.code = code;
    }

    public ResponseCode code() {
        return code;
    }
}
