package com.grepp.spring.infra.error.exceptions.alarm;

import com.grepp.spring.infra.response.ResponseCode;

public class AlarmValidationException extends RuntimeException{

    private final ResponseCode code;

    public AlarmValidationException(ResponseCode code) {
        super(code.message());
        this.code = code;
    }

    public ResponseCode getCode(){
        return code;
    }
}
