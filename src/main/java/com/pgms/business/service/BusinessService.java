package com.pgms.business.service;

import com.pgms.business.dto.BusinessResponse;
import com.pgms.business.dto.CreateBusinessRequest;
import com.pgms.shared.dto.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BusinessService {

  BusinessResponse create(CreateBusinessRequest request);

  PageResponse<BusinessResponse> list(
    String name,
    Boolean active,
    Pageable pageable);

  BusinessResponse getById(UUID id);
}
