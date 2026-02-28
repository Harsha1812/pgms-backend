package com.pgms.business.controller;

import com.pgms.business.dto.BusinessResponse;
import com.pgms.business.dto.CreateBusinessRequest;
import com.pgms.business.service.BusinessService;
import com.pgms.shared.dto.PageResponse;
import com.pgms.shared.idempotency.service.IdempotencyService;
import com.pgms.shared.response.ApiResponse;
import com.pgms.shared.security.OwnerContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/businesses")
@RequiredArgsConstructor
public class BusinessController {

  private final BusinessService businessService;

  private final IdempotencyService idempotencyService;

  @PostMapping
  public ResponseEntity<ApiResponse<BusinessResponse>> create(
    @RequestBody CreateBusinessRequest request,
    @RequestHeader("Idempotency-Key") String key,
    HttpServletRequest httpRequest) {
    UUID ownerId = OwnerContext.getOwnerId();
    BusinessResponse response =
      idempotencyService.execute(
        key,
        httpRequest.getRequestURI(),
        ownerId,
        request,
        () -> businessService.create(request),
        BusinessResponse.class
      );

    return ResponseEntity.ok(
      ApiResponse.success(response, httpRequest.getRequestURI())
    );
  }

  @GetMapping
  public ResponseEntity<ApiResponse<PageResponse<BusinessResponse>>> list(
    @RequestParam(required = false) String name,
    @RequestParam(required = false) Boolean active,
    @PageableDefault(size = 10, sort = "createdAt",
      direction = Sort.Direction.DESC)
    Pageable pageable,
    HttpServletRequest request) {

    PageResponse<BusinessResponse> page =
      businessService.list(name, active, pageable);

    return ResponseEntity.ok(
      ApiResponse.success(page, request.getRequestURI())
    );
  }
}
