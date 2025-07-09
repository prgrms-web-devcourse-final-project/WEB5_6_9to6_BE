package com.grepp.spring.infra.error.exceptions;

public class NotServiceAuthServerException extends RuntimeException {

  public NotServiceAuthServerException() {
    super();
  }

    public NotServiceAuthServerException(String message) {
        super(message);
    }
}
