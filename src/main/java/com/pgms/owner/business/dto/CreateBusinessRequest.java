package com.pgms.owner.business.dto;

public record CreateBusinessRequest(
  String name,
  String code
) {}