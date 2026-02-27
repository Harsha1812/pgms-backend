package com.pgms.shared.dto;

import java.util.UUID;

public record LoginResponse(
  String accessToken,
  String role,
  UUID ownerId,
  UUID branchId
) {}