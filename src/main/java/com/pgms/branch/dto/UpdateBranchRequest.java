package com.pgms.branch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateBranchRequest(
  @Schema(description = "Optimistic locking version", example = "0")
  @NotNull
  Long version,
  String name,
  String city,
  String state,
  String country,
  String addressLine1,
  String addressLine2,
  String pincode,
  Integer totalFloors,
  Integer totalRooms,
  BigDecimal fixedLateFee,
  BigDecimal upfrontDiscountPercentage,
  Integer upfrontDiscountMinMonths,
  BigDecimal referralRewardReferrer,
  BigDecimal referralRewardReferred
) {
  public UpdateBranchRequest {
    fixedLateFee = fixedLateFee == null ? BigDecimal.ZERO : fixedLateFee;
    upfrontDiscountPercentage = upfrontDiscountPercentage == null ? BigDecimal.ZERO : upfrontDiscountPercentage;
    upfrontDiscountMinMonths = upfrontDiscountMinMonths == null ? 0 : upfrontDiscountMinMonths;
    referralRewardReferrer = referralRewardReferrer == null ? BigDecimal.ZERO : referralRewardReferrer;
    referralRewardReferred = referralRewardReferred == null ? BigDecimal.ZERO : referralRewardReferred;
  }
}
