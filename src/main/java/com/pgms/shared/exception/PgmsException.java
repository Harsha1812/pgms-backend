package com.pgms.shared.exception;

import org.springframework.http.HttpStatus;

public abstract class PgmsException extends RuntimeException {

  private final String errorCode;
  private final HttpStatus httpStatus;

  protected PgmsException(String errorCode, String message, HttpStatus status) {
    super(message);
    this.errorCode = errorCode;
    this.httpStatus = status;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}