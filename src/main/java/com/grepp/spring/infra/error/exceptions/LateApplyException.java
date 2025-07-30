package com.grepp.spring.infra.error.exceptions;

import com.grepp.spring.infra.response.ResponseCode;

public class LateApplyException extends CommonException {


    public LateApplyException(ResponseCode code) {
        super(code);
    }

    public LateApplyException(ResponseCode code, Exception e) {
        super(code, e);
    }
}
