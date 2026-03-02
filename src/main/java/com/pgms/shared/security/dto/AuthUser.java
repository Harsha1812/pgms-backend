package com.pgms.shared.security.dto;

import java.util.Set;
import java.util.UUID;

public record AuthUser(
  UUID userId,
  UUID ownerId,
  Set<UUID> branchIds,
  UUID tenantId,
  String role
) {}
