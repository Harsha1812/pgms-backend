package com.pgms.platform.owner.controller;

import com.pgms.platform.owner.service.OwnerService;
import com.pgms.platform.owner.dto.RegisterOwnerRequest;
import com.pgms.platform.owner.dto.RegisterOwnerResponse;
import com.pgms.shared.idempotency.service.IdempotencyService;
import com.pgms.shared.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/platform/owners")
@RequiredArgsConstructor
public class OwnerController {

  private final OwnerService ownerService;

  private final IdempotencyService idempotencyService;

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<RegisterOwnerResponse>> register(
    @RequestBody RegisterOwnerRequest request,
    @RequestHeader("Idempotency-Key") String idempotencyKey,
    HttpServletRequest httpRequest) {

    RegisterOwnerResponse response = idempotencyService.execute(
      idempotencyKey,
      httpRequest.getRequestURI(),
      null, // no owner yet
      request,
      () -> ownerService.register(request),
      RegisterOwnerResponse.class
    );

    return ResponseEntity.ok(
      ApiResponse.success(response, httpRequest.getRequestURI())
    );
  }
}
