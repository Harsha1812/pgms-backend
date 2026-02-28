package com.pgms.branch.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateBranchRequest(
  String name,
  String code,
  String city,
  String state,
  String country,
  String addressLine1,
  String addressLine2,
  String pincode,
  BigDecimal latitude,
  BigDecimal longitude,
  Integer totalFloors,
  Integer totalRooms,
  BigDecimal fixedLateFee,
  BigDecimal upfrontDiscountPercentage,
  Integer upfrontDiscountMinMonths,
  BigDecimal referralRewardReferrer,
  BigDecimal referralRewardReferred
) {
  public CreateBranchRequest {
    fixedLateFee = fixedLateFee == null ? BigDecimal.ZERO : fixedLateFee;
    upfrontDiscountPercentage = upfrontDiscountPercentage == null ? BigDecimal.ZERO : upfrontDiscountPercentage;
    upfrontDiscountMinMonths = upfrontDiscountMinMonths == null ? 0 : upfrontDiscountMinMonths;
    referralRewardReferrer = referralRewardReferrer == null ? BigDecimal.ZERO : referralRewardReferrer;
    referralRewardReferred = referralRewardReferred == null ? BigDecimal.ZERO : referralRewardReferred;
  }
}
