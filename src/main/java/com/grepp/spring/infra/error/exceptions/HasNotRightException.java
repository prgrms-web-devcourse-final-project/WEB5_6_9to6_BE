package com.grepp.spring.infra.error.exceptions;

import com.grepp.spring.infra.response.ResponseCode;

public class HasNotRightException extends CommonException {


    public HasNotRightException(ResponseCode code) {
        super(code);
    }

    public HasNotRightException(ResponseCode code, Exception e) {
        super(code, e);
    }
}
