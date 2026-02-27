package com.pgms.owner.entity;

import com.pgms.shared.base.BaseEntity;
import com.pgms.shared.base.OwnerScopedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "owners")
public class Owner extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String passwordHash;

  @Column(nullable = false)
  private String fullName;

  private String phone;

  @Column(nullable = false)
  private boolean isActive;
}
