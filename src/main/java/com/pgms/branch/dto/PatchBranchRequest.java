package com.pgms.branch.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PatchBranchRequest(

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
  BigDecimal referralRewardReferred,

  @NotNull Long version
) {}
