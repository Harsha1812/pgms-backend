package com.pgms.owner.business.entity;

import com.pgms.shared.base.OwnerScopedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "businesses")
public class Business extends OwnerScopedEntity {

  @Column(nullable = false, length = 150)
  private String name;

  @Column(nullable = false, length = 50)
  private String code;

  @Column(nullable = false)
  private boolean isActive = true;
}
