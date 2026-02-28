package com.pgms.shared.idempotency.repository;

import com.pgms.shared.idempotency.repository.entity.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IdempotencyRepository
  extends JpaRepository<IdempotencyKey, UUID> {

  Optional<IdempotencyKey>
  findByIdempotencyKeyAndEndpoint(
    String idempotencyKey,
    String endpoint
  );

  Optional<IdempotencyKey> findByIdempotencyKeyAndEndpointAndOwnerId(
    String idempotencyKey,
    String endpoint,
    UUID ownerId
  );
}
