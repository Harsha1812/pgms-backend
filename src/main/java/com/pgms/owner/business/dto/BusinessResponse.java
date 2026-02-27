package com.pgms.owner.business.dto;

import java.util.UUID;

public record BusinessResponse(
  UUID id,
  String name,
  String code,
  boolean isActive
) {}
