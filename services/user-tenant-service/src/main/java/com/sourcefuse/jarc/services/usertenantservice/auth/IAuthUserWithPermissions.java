package com.sourcefuse.jarc.services.usertenantservice.auth;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.Data;

/*class creating for testing purpose not required for user tenant serv*/
@Data
public class IAuthUserWithPermissions extends IAuthUser {

  UUID id;

  List<String> permissions;

  int authClientId;

  IUserPrefs userPreferences;

  String email;

  String role;

  String firstName;

  String lastName;

  String middleName;

  UUID tenantId;

  UUID userTenantId;

  Date passwordExpiryTime;
  List<String> allowedResources;
}
