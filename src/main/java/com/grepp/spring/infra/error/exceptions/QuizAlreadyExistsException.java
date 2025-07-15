package com.grepp.spring.infra.error.exceptions;

import com.grepp.spring.infra.response.ResponseCode;

public class QuizAlreadyExistsException extends CommonException {

    public QuizAlreadyExistsException() {
        super(ResponseCode.QUIZ_ALREADY_EXISTS);
    }

    public QuizAlreadyExistsException(String message) {
        super(ResponseCode.QUIZ_ALREADY_EXISTS, new RuntimeException(message));
    }
}