package com.pgms.shared.response;

public record ApiError(
  String code,
  String message,
  Object details
) {}
