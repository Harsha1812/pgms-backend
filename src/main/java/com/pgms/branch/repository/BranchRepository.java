package com.pgms.branch.repository;

import com.pgms.branch.repository.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;
import java.util.UUID;

public interface BranchRepository extends JpaRepository<Branch, UUID>,
  JpaSpecificationExecutor<Branch> {
  boolean existsByBusinessIdAndCode(UUID uuid, String code);

  Optional<Branch> findByIdAndOwnerIdAndBusinessIdAndVersion(
    UUID id,
    UUID ownerId,
    UUID businessId,
    Long version
  );
}
