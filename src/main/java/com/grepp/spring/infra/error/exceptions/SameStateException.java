package com.grepp.spring.infra.error.exceptions;

import com.grepp.spring.infra.response.ResponseCode;

public class SameStateException extends CommonException {


    public SameStateException(ResponseCode code) {
        super(code);
    }

    public SameStateException(ResponseCode code, Exception e) {
        super(code, e);
    }
}
