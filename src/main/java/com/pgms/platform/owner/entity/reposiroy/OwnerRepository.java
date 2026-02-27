package com.pgms.platform.owner.entity.reposiroy;

import com.pgms.platform.owner.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OwnerRepository extends JpaRepository<Owner, UUID> {
  Optional<Owner> findByEmail(String email);

  boolean existsByEmailIgnoreCase(String email);
}
