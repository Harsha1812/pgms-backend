package com.pgms.owner.user;

import com.pgms.shared.base.OwnerScopedEntity;
import com.pgms.shared.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Setter
@Getter
@ToString
@Entity
@Table(name = "users")
public class User extends OwnerScopedEntity {

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String passwordHash;

  @Column(nullable = false)
  private String fullName;

  private String phone;

  @Enumerated(EnumType.STRING)
  private Role role;

  private UUID branchId;

  private boolean isActive;
}
