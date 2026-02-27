package com.pgms.platform.owner.dto;

public record RegisterOwnerRequest(
  String ownerName,
  String email,
  String password,
  String phone
) {}
