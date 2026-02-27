package com.pgms.business.dto;

public record CreateBusinessRequest(
  String name,
  String code
) {}