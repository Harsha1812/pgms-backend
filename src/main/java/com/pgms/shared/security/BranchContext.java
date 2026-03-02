package com.pgms.shared.security;

import java.util.Set;
import java.util.UUID;

public final class BranchContext {

  private static final ThreadLocal<Set<UUID>> CURRENT_BRANCHES =
    new ThreadLocal<>();

  private BranchContext() {}

  public static void setBranchIds(Set<UUID> branchIds) {
    CURRENT_BRANCHES.set(branchIds);
  }

  public static Set<UUID> getBranchIds() {
    return CURRENT_BRANCHES.get();
  }

  public static void clear() {
    CURRENT_BRANCHES.remove();
  }
}