package com.pgms.tenant.auth.login.user.service.impl;

import com.pgms.tenant.auth.login.user.service.UserService;
import com.pgms.tenant.auth.login.user.repository.entity.User;
import com.pgms.tenant.auth.login.user.repository.UserRepository;
import com.pgms.tenant.auth.login.user.dto.LoginRequest;
import com.pgms.tenant.auth.login.user.dto.LoginResponse;
import com.pgms.shared.exception.BusinessValidationException;
import com.pgms.shared.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  @Override
  public LoginResponse login(LoginRequest request) {

    User user = userRepository.findByEmail(request.email())
      .orElseThrow(() ->
        new BusinessValidationException("Invalid credentials"));

    if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
      throw new BusinessValidationException("Invalid credentials");
    }

    if (!user.isActive()) {
      throw new BusinessValidationException("User inactive");
    }

    String token = jwtService.generateToken(user.getId(), user.getOwnerId(), user.getRole().toString());

    return new LoginResponse(
      token,
      user.getRole().name(),
      user.getOwnerId(),
      user.getFullName(),
      user.getBranchId()
    );
  }
}
