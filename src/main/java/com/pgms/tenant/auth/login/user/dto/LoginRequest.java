package com.pgms.tenant.auth.login.user.dto;

public record LoginRequest(
  String email,
  String password
) {}