package com.grepp.spring.infra.error.exceptions;

import com.grepp.spring.infra.response.ResponseCode;

public class NullStateException extends CommonException {


    public NullStateException(ResponseCode code) {
        super(code);
    }

    public NullStateException(ResponseCode code, Exception e) {
        super(code, e);
    }
}
