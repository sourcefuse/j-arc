package com.sourcefuse.jarc.services.usertenantservice.auth;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/*class creating for testing purpose not required for user tenant serv*/
@Data
public class IAuthUserWithPermissions<ID, TID, UTID> extends IAuthUser {
    UUID id;

    ID identifier;

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
