package com.pgms.platform.owner.service;

import com.pgms.platform.owner.entity.Owner;
import com.pgms.platform.owner.entity.reposiroy.OwnerRepository;
import com.pgms.owner.user.User;
import com.pgms.owner.user.repository.UserRepository;
import com.pgms.shared.dto.RegisterOwnerRequest;
import com.pgms.shared.dto.RegisterOwnerResponse;
import com.pgms.shared.enums.Role;
import com.pgms.shared.exception.BusinessValidationException;
import com.pgms.shared.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.pgms.shared.constants.PgmsServiceConstants.SYSTEM_USER_ID;

@Service
@RequiredArgsConstructor
public class OwnerService {

  private final OwnerRepository ownerRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  @Transactional
  public RegisterOwnerResponse register(RegisterOwnerRequest request) {

    if (ownerRepository.existsByEmailIgnoreCase(request.email())) {
      throw new BusinessValidationException("Owner email already exists");
    }

    // 1️⃣ Create Owner
    Owner owner = new Owner();
    owner.setEmail(request.email());
    owner.setFullName(request.ownerName());
    owner.setPhone(request.phone());
    owner.setPasswordHash("N/A"); // owner login not used directly
    owner.setActive(true);

    Owner savedOwner = ownerRepository.save(owner);

    // 2️⃣ Create First Admin User
    User admin = new User();
    admin.setOwnerId(savedOwner.getId());
    admin.setEmail(request.email());
    admin.setPasswordHash(passwordEncoder.encode(request.password()));
    admin.setFullName(request.ownerName());
    admin.setRole(Role.OWNER_ADMIN);
    admin.setActive(true);
    admin.setCreatedBy(SYSTEM_USER_ID);
    admin.setUpdatedBy(SYSTEM_USER_ID);

    userRepository.save(admin);

    // 3️⃣ Generate JWT
    String token = jwtService.generateToken(
      admin.getId(),
      savedOwner.getId(),
      admin.getRole().name()
    );

    return new RegisterOwnerResponse(
      token,
      savedOwner.getId(),
      admin.getRole().name()
    );
  }
}
