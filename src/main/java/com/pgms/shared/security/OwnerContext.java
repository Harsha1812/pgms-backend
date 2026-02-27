package com.pgms.shared.security;

import java.util.UUID;

public final class OwnerContext {

  private static final ThreadLocal<UUID> CURRENT_OWNER = new ThreadLocal<>();

  private OwnerContext() {}

  public static void setOwnerId(UUID ownerId) {
    CURRENT_OWNER.set(ownerId);
  }

  public static UUID getOwnerId() {
    return CURRENT_OWNER.get();
  }

  public static void clear() {
    CURRENT_OWNER.remove();
  }

  public static boolean hasOwner() {
    return CURRENT_OWNER.get() != null;
  }
}