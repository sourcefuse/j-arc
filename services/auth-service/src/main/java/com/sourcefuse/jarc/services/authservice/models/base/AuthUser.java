package com.sourcefuse.jarc.services.authservice.models.base;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.sourcefuse.jarc.services.authservice.enums.UserStatus;
import com.sourcefuse.jarc.services.authservice.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser
    extends User
    implements
    com.sourcefuse.jarc.services.authservice.models.base.AuthUserWithPermissions<UUID, UUID, UUID> {

  UUID id;
  String username;
  String password;
  UUID identifier;
  List<String> permissions;
  int authClientId;
  String email;
  String role;
  String firstName;
  String lastName;
  String middleName;
  UUID tenantId;
  UUID userTenantId;
  Date passwordExpiryTime;
  List<String> allowedResources;
  String externalAuthToken;
  int age;
  String externalRefreshToken;
  UserStatus status;
  // IUserPref userPreferences;
  // DeviceInfo deviceInfo;
}
