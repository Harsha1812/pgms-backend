package com.pgms.branch.controller;


import com.pgms.branch.dto.BranchResponse;
import com.pgms.branch.dto.CreateBranchRequest;
import com.pgms.branch.dto.UpdateBranchRequest;
import com.pgms.branch.service.BranchService;
import com.pgms.business.dto.BusinessResponse;
import com.pgms.shared.dto.PageResponse;
import com.pgms.shared.idempotency.service.IdempotencyService;
import com.pgms.shared.response.ApiResponse;
import com.pgms.shared.security.OwnerContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/businesses/{businessId}/branches")
@RequiredArgsConstructor
public class BranchController {

  private final BranchService branchService;

  private final IdempotencyService idempotencyService;

  // =============================
  // CREATE BRANCH (IDEMPOTENT)
  // =============================
  @PostMapping
  public ResponseEntity<ApiResponse<BranchResponse>> create(
    @PathVariable UUID businessId,
    @RequestHeader(value = "Idempotency-Key", required = false)
    String idempotencyKey,
    @RequestBody CreateBranchRequest request,
    HttpServletRequest httpRequest) {
    UUID ownerId = OwnerContext.getOwnerId();
    BranchResponse branchResponse = idempotencyService.execute(
      idempotencyKey,
      httpRequest.getRequestURI(),
      ownerId,
      request,
      () -> branchService.create(request, businessId),
      BranchResponse.class
    );

    return ResponseEntity.ok(
      ApiResponse.success(branchResponse, httpRequest.getRequestURI())
    );
  }

  // =============================
  // LIST BRANCHES
  // =============================
  @GetMapping
  public ResponseEntity<ApiResponse<PageResponse<BranchResponse>>> list(
    @PathVariable UUID businessId,
    @RequestParam(required = false) Boolean active,
    @RequestParam(required = false) String city,
    @RequestParam(required = false) String name,
    @RequestParam(required = false) String state,
    @PageableDefault(
      size = 10,
      sort = "createdAt",
      direction = Sort.Direction.DESC
    ) Pageable pageable,
    HttpServletRequest httpRequest) {

    PageResponse<BranchResponse> response =
      branchService.list(businessId, name, active, city, state, pageable);

    return ResponseEntity.ok(
      ApiResponse.success(response, httpRequest.getRequestURI())
    );
  }

  // =============================
  // GET BRANCH BY ID
  // =============================
  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<BranchResponse>> get(
    @PathVariable UUID id,
    HttpServletRequest httpRequest) {

    BranchResponse response = branchService.get(id);

    return ResponseEntity.ok(
      ApiResponse.success(response, httpRequest.getRequestURI())
    );
  }

  @PutMapping("/{branchId}")
  public ResponseEntity<ApiResponse<Void>> update(
    @PathVariable UUID businessId,
    @PathVariable UUID branchId,
    @RequestHeader(value = "Idempotency-Key", required = false)
    String idempotencyKey,
    @RequestBody UpdateBranchRequest request,
    HttpServletRequest httpRequest) {
    UUID ownerId = OwnerContext.getOwnerId();
    branchService.update(businessId, branchId, request);
    idempotencyService.execute(
      idempotencyKey,
      httpRequest.getRequestURI(),
      ownerId,
      request,
      () -> branchService.update(businessId, branchId, request),
      BranchResponse.class
    );
    return ResponseEntity.ok(
      ApiResponse.success(null, httpRequest.getRequestURI())
    );
  }
}
