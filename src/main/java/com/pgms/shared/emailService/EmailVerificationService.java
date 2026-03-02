package com.pgms.shared.emailService;

import com.pgms.tenant.auth.login.user.enums.UserStatus;
import com.pgms.tenant.auth.login.user.repository.entity.User;

public class EmailVerificationService {

  private void tryActivate(User user) {
    if (user.isApproved() && user.isEmailVerified()) {
      user.setStatus(UserStatus.ACTIVE);
    }
  }
}
