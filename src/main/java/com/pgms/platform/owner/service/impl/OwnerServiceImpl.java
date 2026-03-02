package com.pgms.platform.owner.service.impl;

import com.pgms.platform.owner.repository.entity.Owner;
import com.pgms.platform.owner.repository.OwnerRepository;
import com.pgms.platform.owner.service.OwnerService;
import com.pgms.shared.security.CryptoService;
import com.pgms.tenant.auth.login.user.enums.UserStatus;
import com.pgms.tenant.auth.login.user.repository.entity.User;
import com.pgms.tenant.auth.login.user.repository.UserRepository;
import com.pgms.platform.owner.dto.RegisterOwnerRequest;
import com.pgms.platform.owner.dto.RegisterOwnerResponse;
import com.pgms.shared.enums.Role;
import com.pgms.shared.exception.BusinessValidationException;
import com.pgms.shared.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.pgms.shared.constants.PgmsServiceConstants.SYSTEM_USER_ID;

@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {

  private final OwnerRepository ownerRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final CryptoService cryptoService;

  @Transactional
  @Override
  public RegisterOwnerResponse register(RegisterOwnerRequest request) {
    String normalizedEmail = request.email().trim().toLowerCase();
    String emailHash = cryptoService.sha256(normalizedEmail);

    if (ownerRepository.existsByEmailHash(emailHash)) {
      throw new BusinessValidationException("Owner email already exists");
    }

    String encryptedEmail = cryptoService.encrypt(request.email());
    String encryptedPhone = cryptoService.encrypt(request.phone());

    // 1️⃣ Create Owner
    Owner owner = new Owner();
    owner.setEmail(encryptedEmail);
    owner.setFullName(request.ownerName());
    owner.setPhone(encryptedPhone);
    owner.setActive(true);


    Owner savedOwner = ownerRepository.save(owner);

    // 2️⃣ Create First Admin User
    User admin = new User();
    admin.setOwnerId(savedOwner.getId());
    admin.setEmail(encryptedEmail);
    admin.setEmailHash(emailHash);
    admin.setPhone(encryptedPhone);
    admin.setPasswordHash(passwordEncoder.encode(request.password()));
    admin.setFullName(request.ownerName());
    admin.setRole(Role.OWNER_ADMIN);
    admin.setActive(true);
    admin.setCreatedBy(SYSTEM_USER_ID);
    admin.setUpdatedBy(SYSTEM_USER_ID);
    admin.setStatus(UserStatus.PENDING);
    admin.setApproved(true);
    admin.setEmailVerified(false);

    userRepository.save(admin);

    // 3️⃣ Generate JWT
    String token = jwtService.generateToken(
      admin.getId(),
      savedOwner.getId(),
      admin.getRole().name(),
      List.of(),
      null
    );
    return new RegisterOwnerResponse(
      null,
      savedOwner.getId(),
      admin.getRole().name(),
      true // email verification required
    );
/*    return new RegisterOwnerResponse(
      token,
      savedOwner.getId(),
      admin.getRole().name()
    );*/
  }
}
