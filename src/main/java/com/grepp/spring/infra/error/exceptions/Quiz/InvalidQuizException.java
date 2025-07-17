package com.grepp.spring.infra.error.exceptions.Quiz;

public class InvalidQuizException extends RuntimeException {

    public InvalidQuizException(String message) {
      super(message);
  }
}
