package com.grepp.spring.infra.error.exceptions;

import com.grepp.spring.infra.response.ResponseCode;

public class AlreadyProcessedException extends CommonException {


    public AlreadyProcessedException(ResponseCode code) {
        super(code);
    }

    public AlreadyProcessedException(ResponseCode code, Exception e) {
        super(code, e);
    }
}
