package com.pgms.business.service.impl;

import com.pgms.business.dto.BusinessResponse;
import com.pgms.business.dto.CreateBusinessRequest;
import com.pgms.business.repository.BusinessSpecifications;
import com.pgms.business.repository.entity.Business;
import com.pgms.business.repository.BusinessRepository;
import com.pgms.business.service.BusinessService;
import com.pgms.shared.dto.PageResponse;
import com.pgms.shared.exception.BusinessValidationException;
import com.pgms.shared.exception.ResourceNotFoundException;
import com.pgms.shared.security.OwnerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

  private final BusinessRepository repository;

  private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
    "name",
    "code",
    "createdAt",
    "isActive"
  );

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
      saved.isActive(),
      business.getVersion());
  }

  @Override
  public PageResponse<BusinessResponse> list(
    String name,
    Boolean active,
    Pageable pageable) {

    Pageable safePageable = buildSafePageable(pageable);
    UUID ownerId = OwnerContext.getOwnerId();

    Specification<Business> spec =
      BusinessSpecifications.byOwner(ownerId);

    if (name != null && !name.isBlank()) {
      spec = spec.and(
        BusinessSpecifications.nameContains(name)
      );
    }

    if (active != null) {
      spec = spec.and(
        BusinessSpecifications.isActive(active)
      );
    }
    Page<Business> pageResult =
      repository.findAll(spec, safePageable);
    return PageResponse.from(
      pageResult,
      this::toResponse);
  }

  @Override
  public BusinessResponse getById(UUID id) {
    UUID ownerId = OwnerContext.getOwnerId();

    Business business = repository
      .findByIdAndOwnerId(id, ownerId)
      .orElseThrow(() -> new ResourceNotFoundException(
        "Business not found"
      ));

    return toResponse(business);
  }

  private BusinessResponse toResponse(Business business) {
    return new BusinessResponse(
      business.getId(),
      business.getName(),
      business.getCode(),
      business.isActive(),
      business.getVersion()
    );
  }

  private Pageable buildSafePageable(Pageable pageable) {

    List<Sort.Order> safeOrders = new ArrayList<>();

    for (Sort.Order order : pageable.getSort()) {

      String property = order.getProperty();

      if (!ALLOWED_SORT_FIELDS.contains(property)) {
        throw new BusinessValidationException(
          "Invalid sort field: " + property
        );
      }

      safeOrders.add(order);
    }

    Sort safeSort = safeOrders.isEmpty()
      ? Sort.by(Sort.Direction.DESC, "createdAt")
      : Sort.by(safeOrders);

    return PageRequest.of(
      pageable.getPageNumber(),
      pageable.getPageSize(),
      safeSort
    );
  }
}
