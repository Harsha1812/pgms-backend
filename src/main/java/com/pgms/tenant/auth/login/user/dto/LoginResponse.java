package com.pgms.tenant.auth.login.user.dto;

import java.util.List;
import java.util.UUID;

public record LoginResponse(
  String accessToken,
  String role,
  UUID ownerId,
  String fullName,
  List<UUID> branchIds
) {}