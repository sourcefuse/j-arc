package com.sourcefuse.jarc.services.usertenantservice.mocks;

import com.google.gson.Gson;
import com.sourcefuse.jarc.core.enums.Gender;
import com.sourcefuse.jarc.core.enums.RoleKey;
import com.sourcefuse.jarc.core.enums.TenantStatus;
import com.sourcefuse.jarc.core.enums.UserStatus;
import com.sourcefuse.jarc.services.usertenantservice.dto.Role;
import com.sourcefuse.jarc.services.usertenantservice.dto.Tenant;
import com.sourcefuse.jarc.services.usertenantservice.dto.User;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserDto;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenant;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenantPrefs;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import java.util.ArrayList;
import java.util.UUID;
import org.springframework.beans.BeanUtils;

public final class MockTenantUser {

  private MockTenantUser() {}

  public static final UUID ROLE_ID = UUID.randomUUID();
  public static final UUID TENANT_ID = UUID.randomUUID();
  public static final UUID USER_TENANT_ID = UUID.randomUUID();
  public static final UUID USER_ID = UUID.randomUUID();

  public static UserDto getUserDtoObj() {
    UserDto userDto = new UserDto();
    userDto.setRoleId(ROLE_ID);
    userDto.setTenantId(TENANT_ID);
    userDto.setStatus(RoleKey.DEFAULT.ordinal());
    userDto.setAuthId("authId");
    userDto.setAuthProvider("Keycloak");
    userDto.setUserTenantId(USER_TENANT_ID);

    User user = getUserObj();
    userDto.setUserDetails(user);
    return userDto;
  }

  public static User getUserObj() {
    User user = new User();
    user.setId(USER_ID);
    user.setFirstName("Messi");
    user.setLastName("lionel");
    user.setMiddleName("john");
    user.setUsername("Messi");
    user.setEmail("fc@barca.in");
    user.setAuthClientIds("authid");
    user.setGender(Gender.MALE);
    user.setDefaultTenant(new Tenant(TENANT_ID));
    return user;
  }

  public static UserView getUserViewObj() {
    UserView userView = new UserView();
    BeanUtils.copyProperties(getUserObj(), userView);
    userView.setTenantId(TENANT_ID);
    userView.setRoleId(ROLE_ID);
    userView.setStatus(UserStatus.ACTIVE);
    return userView;
  }

  public static UserTenant getUserTenantObj() {
    UserTenant userTenant = new UserTenant();
    userTenant.setRole(new Role(ROLE_ID));
    userTenant.setTenant(new Tenant(TENANT_ID));
    userTenant.setUser(new User(USER_ID));
    userTenant.setStatus(UserStatus.ACTIVE);
    return userTenant;
  }

  public static Tenant geTenantObj() {
    Tenant tenant = new Tenant();
    tenant.setId(TENANT_ID);
    tenant.setName("Test Tenant");
    tenant.setStatus(TenantStatus.ACTIVE);
    tenant.setKey("uniqueKey");
    tenant.setAddress("Juhu");
    tenant.setState("Maharashtra");
    tenant.setPrimaryContactEmail("@source.com");
    tenant.setAllowedDomain("@sourceFuse");
    tenant.setTenantConfigs(new ArrayList<>());
    tenant.setUserTenants(new ArrayList<>());
    return tenant;
  }

  public static UserTenantPrefs geUserTenantPrefsObj() {
    UserTenantPrefs userTenantPrefs = new UserTenantPrefs();
    userTenantPrefs.setConfigKey("last-access-url");
    userTenantPrefs.setConfigValue(new Gson().toJson("test"));
    userTenantPrefs.setUserTenant(
      new UserTenant(MockTenantUser.USER_TENANT_ID)
    );
    return userTenantPrefs;
  }
}
