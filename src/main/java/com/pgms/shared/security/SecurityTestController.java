package com.pgms.owner.auth;

import com.pgms.shared.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/test")
public class SecurityTestController {

  @GetMapping
  public ResponseEntity<ApiResponse<String>> test(
    Authentication authentication,
    HttpServletRequest request) {

    UUID userId = (UUID) authentication.getPrincipal();

    return ResponseEntity.ok(
      ApiResponse.success(
        "Authenticated user: " + userId,
        request.getRequestURI()
      )
    );
  }
}