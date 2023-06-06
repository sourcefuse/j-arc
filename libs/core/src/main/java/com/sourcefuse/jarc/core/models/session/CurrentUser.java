package com.sourcefuse.jarc.core.models.session;

import java.util.List;
import java.util.UUID;
import com.sourcefuse.jarc.core.enums.RoleKey;
import com.sourcefuse.jarc.core.enums.UserStatus;
import com.sourcefuse.jarc.core.models.base.BaseAuthUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUser implements BaseAuthUser {
  UUID id;
  String username;
  UUID userTenantId;
  UUID roleId;
  List<String> permissions;
  List<UUID> authClientIds;
  String email;
  UserStatus status;
  UUID tenantId;

  String firstName;
  String middleName;
  String lastName;
  
  UUID defaultTenantId;
  RoleKey roleType;
  
}
