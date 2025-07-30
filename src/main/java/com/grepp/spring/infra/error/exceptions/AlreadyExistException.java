package com.grepp.spring.infra.error.exceptions;

import com.grepp.spring.infra.response.ResponseCode;

public class AlreadyExistException extends CommonException {


    public AlreadyExistException(ResponseCode code) {
        super(code);
    }

    public AlreadyExistException(ResponseCode code, Exception e) {
        super(code, e);
    }
}
