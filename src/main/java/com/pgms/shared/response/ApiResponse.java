package com.pgms.shared.response;

import java.time.LocalDateTime;

public record ApiResponse<T>(
  boolean success,
  T data,
  ApiError error,
  String version,
  LocalDateTime timestamp,
  String path
) {

  public static <T> ApiResponse<T> success(T data, String path) {
    return new ApiResponse<>(
      true,
      data,
      null,
      "v1",
      LocalDateTime.now(),
      path
    );
  }

  public static <T> ApiResponse<T> failure(ApiError error, String path) {
    return new ApiResponse<>(
      false,
      null,
      error,
      "v1",
      LocalDateTime.now(),
      path
    );
  }
}