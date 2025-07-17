package com.grepp.spring.infra.error.exceptions;

public class InvalidQuizException extends RuntimeException {

    public InvalidQuizException(String message) {
      super(message);
  }
}
