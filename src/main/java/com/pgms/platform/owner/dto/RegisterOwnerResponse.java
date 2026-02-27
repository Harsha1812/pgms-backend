package com.pgms.platform.owner.dto;

import java.util.UUID;

public record RegisterOwnerResponse(
  String accessToken,
  UUID ownerId,
  String role
) {}
