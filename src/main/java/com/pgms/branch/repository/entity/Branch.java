package com.pgms.branch.repository.entity;

import com.pgms.shared.base.OwnerScopedEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "branches",
  uniqueConstraints = {
    @UniqueConstraint(
      name = "uk_branch_code",
      columnNames = {"business_id", "code"}
    )
  })
public class Branch extends OwnerScopedEntity {

  @Column(nullable = false)
  private UUID businessId;

  @Column(nullable = false, length = 150)
  private String name;

  @Column(nullable = false, length = 50)
  private String code;

  @Column(nullable = false)
  private String city;

  @Column(nullable = false)
  private String state;

  @Column(nullable = false)
  private String country;

  @Column(nullable = false)
  private String addressLine1;

  private String addressLine2;
  private String pincode;

  private BigDecimal latitude;
  private BigDecimal longitude;

  private Integer totalFloors;
  private Integer totalRooms;

  @Column(nullable = false)
  private BigDecimal fixedLateFee = BigDecimal.ZERO;

  @Column(nullable = false)
  private BigDecimal upfrontDiscountPercentage = BigDecimal.ZERO;

  @Column(nullable = false)
  private Integer upfrontDiscountMinMonths = 0;

  @Column(nullable = false)
  private BigDecimal referralRewardReferrer = BigDecimal.ZERO;

  @Column(nullable = false)
  private BigDecimal referralRewardReferred = BigDecimal.ZERO;

  private boolean isActive = true;
}
