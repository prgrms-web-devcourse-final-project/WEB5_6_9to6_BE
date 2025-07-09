package com.grepp.spring.infra.error.exceptions;

import com.grepp.spring.infra.response.ResponseCode;

public class MailSendFailureException extends CommonException {

    public MailSendFailureException(ResponseCode code) {
        super(code);
    }

    public MailSendFailureException(ResponseCode code, Exception e) {
        super(code, e);
    }

}
