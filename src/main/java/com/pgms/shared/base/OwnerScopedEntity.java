package com.pgms.shared.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class OwnerScopedEntity extends AuditableEntity {

  @Column(nullable = false)
  private UUID ownerId;
}
