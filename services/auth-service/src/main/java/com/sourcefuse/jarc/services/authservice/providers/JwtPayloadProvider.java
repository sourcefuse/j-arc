package com.sourcefuse.jarc.services.authservice.providers;

import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.services.authservice.models.Role;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import org.springframework.stereotype.Service;

@Service
public class JwtPayloadProvider {

  public CurrentUser provide(User user, UserTenant userTenant, Role role) {
    CurrentUser currentUser = new CurrentUser();
    currentUser.setId(user.getId());
    currentUser.setUsername(user.getUsername());
    currentUser.setUserTenantId(userTenant.getId());
    currentUser.setRoleId(role.getId());
    currentUser.setPermissions(role.getPermissions());
    currentUser.setAuthClientIds(user.getAuthClientIds());
    currentUser.setEmail(user.getEmail());
    currentUser.setStatus(userTenant.getStatus());
    currentUser.setTenantId(userTenant.getTenantId());
    currentUser.setFirstName(user.getFirstName());
    currentUser.setMiddleName(user.getMiddleName());
    currentUser.setLastName(user.getLastName());
    currentUser.setDefaultTenantId(user.getDefaultTenantId());
    currentUser.setRoleType(role.getRoleType());
    return currentUser;
  }
}
