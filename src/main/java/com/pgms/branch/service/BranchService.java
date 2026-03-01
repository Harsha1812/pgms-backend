package com.pgms.branch.service;

import com.pgms.branch.dto.BranchResponse;
import com.pgms.branch.dto.CreateBranchRequest;
import com.pgms.branch.dto.PatchBranchRequest;
import com.pgms.branch.dto.UpdateBranchRequest;
import com.pgms.shared.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BranchService {
  BranchResponse create(CreateBranchRequest request, UUID businessId);

  PageResponse<BranchResponse> list(UUID businessId,
                                    String name,
                                    Boolean active,
                                    String city,
                                    String state,
                                    Pageable pageable);

  BranchResponse get(UUID id);

  BranchResponse update(UUID businessId, UUID branchId, UpdateBranchRequest request);

  BranchResponse patch(
    UUID businessId,
    UUID branchId,
    PatchBranchRequest request);
}
