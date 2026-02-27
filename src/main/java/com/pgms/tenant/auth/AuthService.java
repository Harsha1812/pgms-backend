package com.pgms.tenant.auth;

import com.pgms.owner.user.User;
import com.pgms.owner.user.repository.UserRepository;
import com.pgms.shared.dto.LoginRequest;
import com.pgms.shared.dto.LoginResponse;
import com.pgms.shared.exception.BusinessValidationException;
import com.pgms.shared.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

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
      user.getBranchId()
    );
  }
}
