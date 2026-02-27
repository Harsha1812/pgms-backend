package com.pgms.shared.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class AuditableEntity extends BaseEntity {

  @Column(nullable = false, updatable = false)
  private UUID createdBy;

  @Column(nullable = false)
  private UUID updatedBy;
}
