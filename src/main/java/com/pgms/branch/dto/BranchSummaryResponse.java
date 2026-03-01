package com.pgms.branch.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record BranchSummaryResponse(
  UUID id,
  String name,
  String code,
  String city,
  long totalRooms,
  long occupiedBeds,
  long totalBeds,
  int occupancyRate,
  BigDecimal monthlyRevenue
) {}
