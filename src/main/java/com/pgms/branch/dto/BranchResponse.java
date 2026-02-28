package com.pgms.branch.dto;

import java.util.UUID;

public record BranchResponse(
  UUID id,
  String name,
  String code,
  boolean isActive,
  Long version) {}
