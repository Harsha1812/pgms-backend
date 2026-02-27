package com.pgms.shared.exception;

import com.pgms.shared.response.ApiError;
import com.pgms.shared.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(PgmsException.class)
  public ResponseEntity<ApiResponse<Void>> handlePgmsException(
    PgmsException ex,
    HttpServletRequest request) {

    ApiError error = new ApiError(
      ex.getErrorCode(),
      ex.getMessage(),
      null
    );

    return ResponseEntity
      .status(ex.getHttpStatus())
      .body(ApiResponse.failure(error, request.getRequestURI()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleGenericException(
    Exception ex,
    HttpServletRequest request) {

    log.error("Unhandled exception", ex);

    ApiError error = new ApiError(
      "INTERNAL_SERVER_ERROR",
      "Something went wrong. Please contact support.",
      null
    );

    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(ApiResponse.failure(error, request.getRequestURI()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handleValidationException(
    MethodArgumentNotValidException ex,
    HttpServletRequest request) {

    Map<String, String> errors = ex.getBindingResult()
      .getFieldErrors()
      .stream()
      .collect(Collectors.toMap(
        FieldError::getField,
        FieldError::getDefaultMessage
      ));

    ApiError error = new ApiError(
      "VALIDATION_FAILED",
      "Validation failed",
      errors
    );

    return ResponseEntity
      .badRequest()
      .body(ApiResponse.failure(error, request.getRequestURI()));
  }
}
