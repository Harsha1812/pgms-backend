package com.pgms.tenant.auth.login.user.controller;

import com.pgms.tenant.auth.login.user.dto.LoginRequest;
import com.pgms.tenant.auth.login.user.dto.LoginResponse;
import com.pgms.shared.response.ApiResponse;
import com.pgms.tenant.auth.login.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request,
     HttpServletRequest httpRequest) {

    LoginResponse response = userService.login(
      new LoginRequest(request.email(),
      request.password()
      )    );

    return ResponseEntity.ok(
      ApiResponse.success(response, httpRequest.getRequestURI())
    );
  }
}