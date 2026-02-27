package com.pgms.shared.audit;

import com.pgms.shared.base.AuditableEntity;
import com.pgms.shared.base.OwnerScopedEntity;
import com.pgms.shared.security.OwnerContext;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.UUID;

public class AuditEntityListener {

  @PrePersist
  public void onPrePersist(Object entity) {

    if (entity instanceof AuditableEntity auditable) {

      LocalDateTime now = LocalDateTime.now();

      auditable.setCreatedAt(now);
      auditable.setUpdatedAt(now);

      UUID userId = getCurrentUserId();

      if (userId != null) {
        auditable.setCreatedBy(userId);
        auditable.setUpdatedBy(userId);
      }

      if (entity instanceof OwnerScopedEntity ownerScoped) {
        if (OwnerContext.hasOwner()) {
          ownerScoped.setOwnerId(OwnerContext.getOwnerId());
        }
      }
    }
  }

  @PreUpdate
  public void onPreUpdate(Object entity) {

    if (entity instanceof AuditableEntity auditable) {

      auditable.setUpdatedAt(LocalDateTime.now());

      UUID userId = getCurrentUserId();

      if (userId != null) {
        auditable.setUpdatedBy(userId);
      }
    }
  }

  private UUID getCurrentUserId() {

    Authentication authentication =
      SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      return null;
    }

    Object principal = authentication.getPrincipal();

    if (principal instanceof UUID uuid) {
      return uuid;
    }

    return null;
  }
}