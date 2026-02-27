package com.pgms.shared.exception;

import org.springframework.http.HttpStatus;

public class BusinessValidationException extends PgmsException {

  public BusinessValidationException(String message) {
    super("VALIDATION_FAILED", message, HttpStatus.BAD_REQUEST);
  }
}
