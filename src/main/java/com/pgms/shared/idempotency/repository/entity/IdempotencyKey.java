package com.pgms.shared.idempotency.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
  name = "idempotency_keys",
  uniqueConstraints = @UniqueConstraint(
    columnNames = {"idempotencyKey", "endpoint"}
  )
)
@Getter
@Setter
public class IdempotencyKey {

  @Id
  private UUID id;

  private String idempotencyKey;

  private UUID ownerId;

  private String endpoint;

  private String requestHash;

  @Column(columnDefinition = "TEXT")
  private String responseBody;

  private Integer statusCode;

  private LocalDateTime createdAt;
}
