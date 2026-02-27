package com.pgms.business.service;

import com.pgms.business.dto.BusinessResponse;
import com.pgms.business.dto.CreateBusinessRequest;

public interface BusinessService {

  BusinessResponse create(CreateBusinessRequest request);
}
