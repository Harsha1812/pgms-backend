package com.pgms.owner.business.service;

import com.pgms.owner.business.dto.BusinessResponse;
import com.pgms.owner.business.dto.CreateBusinessRequest;
import com.pgms.owner.business.entity.Business;
import com.pgms.owner.business.repository.BusinessRepository;
import com.pgms.shared.exception.BusinessValidationException;
import com.pgms.shared.security.OwnerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BusinessService {

  private final BusinessRepository repository;

  public BusinessResponse create(CreateBusinessRequest request) {

    UUID ownerId = OwnerContext.getOwnerId();

    if (repository.existsByCodeAndOwnerId(request.code(), ownerId)) {
      throw new BusinessValidationException("Business code already exists");
    }

    Business business = new Business();
    business.setName(request.name());
    business.setCode(request.code());
    business.setActive(true);

    Business saved = repository.save(business);

    return new BusinessResponse(
      saved.getId(),
      saved.getName(),
      saved.getCode(),
      saved.isActive()
    );
  }
}
