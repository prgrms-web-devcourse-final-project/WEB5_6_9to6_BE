package com.grepp.spring.infra.error.exceptions;

import com.grepp.spring.infra.response.ResponseCode;

public class InvalidQuizException extends CommonException {

  public InvalidQuizException() {
    super(ResponseCode.INVALID_QUIZ);
  }

  public InvalidQuizException(String message) {
    super(ResponseCode.INVALID_QUIZ, new RuntimeException(message));
  }
}
