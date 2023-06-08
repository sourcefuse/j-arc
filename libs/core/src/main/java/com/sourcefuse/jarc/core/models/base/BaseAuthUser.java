package com.sourcefuse.jarc.core.models.base;

import java.util.List;
import java.util.UUID;

import com.sourcefuse.jarc.core.enums.RoleKey;
import com.sourcefuse.jarc.core.enums.UserStatus;

public interface BaseAuthUser {
  UUID getId();
  String getUsername();
  UUID getUserTenantId();
  UUID getRoleId();
  List<String> getPermissions();
  List<UUID> getAuthClientIds();
  String getEmail();
  UserStatus getStatus();
  UUID getTenantId();

  String getFirstName();
  String getMiddleName();
  String getLastName();
  
  UUID getDefaultTenantId();
  RoleKey getRoleType();
  
}
