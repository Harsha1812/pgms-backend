package com.pgms.tenant.auth.login.user.service.impl;

import com.pgms.shared.security.CryptoService;
import com.pgms.tenant.auth.login.user.enums.UserStatus;
import com.pgms.tenant.auth.login.user.service.UserService;
import com.pgms.tenant.auth.login.user.repository.entity.User;
import com.pgms.tenant.auth.login.user.repository.UserRepository;
import com.pgms.tenant.auth.login.user.dto.LoginRequest;
import com.pgms.tenant.auth.login.user.dto.LoginResponse;
import com.pgms.shared.exception.BusinessValidationException;
import com.pgms.shared.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.pgms.tenant.auth.login.user.enums.UserStatus.ACTIVE;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final CryptoService cryptoService;
  @Value("${pgms.security.allow-unverified-login:false}")
  private boolean allowUnverifiedLogin;

  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, CryptoService cryptoService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.cryptoService = cryptoService;
  }

  @Override
  public LoginResponse login(LoginRequest request) {

    String normalizedEmail = request.email().trim().toLowerCase();
    String hash = cryptoService.sha256(normalizedEmail);

    User user = userRepository.findByEmailHash(hash)
      .orElseThrow(() -> new BusinessValidationException("Invalid credentials"));

    if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
      throw new BusinessValidationException("Invalid credentials");
    }

    if (!user.isApproved()) {
      throw new BusinessValidationException("Account not approved");
    }

    if (user.getStatus() != ACTIVE) {
      throw new BusinessValidationException("Account not active");
    }

    if (!user.isEmailVerified() && !allowUnverifiedLogin) {
      throw new BusinessValidationException("Email not verified");
    }

    if (!user.isActive()) {
      throw new BusinessValidationException("User inactive");
    }

    List<UUID> branchIds = user.getBranchId() != null
      ? List.of(user.getBranchId())
      : List.of();

    UUID tenantId = user.getTenantId();

    String token = jwtService.generateToken(
      user.getId(),
      user.getOwnerId(),
      user.getRole().name(),
      branchIds,
      tenantId
    );

    return new LoginResponse(
      token,
      user.getRole().name(),
      user.getOwnerId(),
      user.getFullName(),
      branchIds
    );
  }

}
