package com.pgms.tenant.auth.controller;

import com.pgms.shared.dto.LoginRequest;
import com.pgms.shared.dto.LoginResponse;
import com.pgms.tenant.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public Map<String, String> login(@RequestBody LoginRequest request) {

    LoginResponse token = authService.login(
      new LoginRequest(request.email(),
      request.password()
      )    );

    return Map.of("accessToken", token.accessToken());
  }
}