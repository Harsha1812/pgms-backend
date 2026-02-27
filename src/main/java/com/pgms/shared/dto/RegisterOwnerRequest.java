package com.pgms.shared.dto;

public record RegisterOwnerRequest(
  String ownerName,
  String email,
  String password,
  String phone
) {}
