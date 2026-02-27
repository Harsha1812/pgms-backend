package com.pgms.business.service.impl;

import com.pgms.business.dto.BusinessResponse;
import com.pgms.business.dto.CreateBusinessRequest;
import com.pgms.business.repository.entity.Business;
import com.pgms.business.repository.BusinessRepository;
import com.pgms.business.service.BusinessService;
import com.pgms.shared.exception.BusinessValidationException;
import com.pgms.shared.security.OwnerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

  private final BusinessRepository repository;

  @Override
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
