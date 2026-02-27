package com.pgms.platform.owner.repository.entity;

import com.pgms.shared.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
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
