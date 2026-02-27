package com.pgms.shared.dto;

public record LoginRequest(
  String email,
  String password
) {}