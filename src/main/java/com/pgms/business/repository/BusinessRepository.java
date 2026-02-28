package com.pgms.business.repository;

import com.pgms.business.repository.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface BusinessRepository extends JpaRepository<Business, UUID>, JpaSpecificationExecutor<Business> {

  Optional<Business> findByIdAndOwnerId(UUID id, UUID ownerId);

  boolean existsByCodeAndOwnerId(String code, UUID ownerId);
}
