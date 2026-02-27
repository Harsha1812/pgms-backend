package com.pgms.shared.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends PgmsException {

  public ResourceNotFoundException(String message) {
    super("RESOURCE_NOT_FOUND", message, HttpStatus.NOT_FOUND);
  }
}
