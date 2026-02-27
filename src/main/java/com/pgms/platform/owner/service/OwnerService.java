package com.pgms.platform.owner.service;

import com.pgms.platform.owner.dto.RegisterOwnerRequest;
import com.pgms.platform.owner.dto.RegisterOwnerResponse;

public interface OwnerService {

  RegisterOwnerResponse register(RegisterOwnerRequest request);
}
