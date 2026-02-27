package com.pgms.business.controller;

import com.pgms.business.dto.BusinessResponse;
import com.pgms.business.dto.CreateBusinessRequest;
import com.pgms.business.service.BusinessService;
import com.pgms.shared.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/businesses")
@RequiredArgsConstructor
public class BusinessController {

  private final BusinessService service;

  @PostMapping
  public ResponseEntity<ApiResponse<BusinessResponse>> create(
    @RequestBody CreateBusinessRequest request,
    HttpServletRequest httpRequest) {

    BusinessResponse response = service.create(request);

    return ResponseEntity.ok(
      ApiResponse.success(response, httpRequest.getRequestURI())
    );
  }
}
