package com.pgms.platform.owner.controller;

import com.pgms.platform.owner.service.OwnerService;
import com.pgms.platform.owner.dto.RegisterOwnerRequest;
import com.pgms.platform.owner.dto.RegisterOwnerResponse;
import com.pgms.shared.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/platform/owners")
@RequiredArgsConstructor
public class OwnerController {

  private final OwnerService ownerService;

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<RegisterOwnerResponse>> register(
    @RequestBody RegisterOwnerRequest request,
    HttpServletRequest httpRequest) {

    RegisterOwnerResponse response = ownerService.register(request);

    return ResponseEntity.ok(
      ApiResponse.success(response, httpRequest.getRequestURI())
    );
  }
}
