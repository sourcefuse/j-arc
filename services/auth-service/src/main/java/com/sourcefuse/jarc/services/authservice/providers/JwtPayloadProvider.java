package com.sourcefuse.jarc.services.authservice.providers;

import com.sourcefuse.jarc.services.authservice.models.Role;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import com.sourcefuse.jarc.services.authservice.session.CurrentUser;
import org.springframework.stereotype.Service;

@Service
public class JwtPayloadProvider {

  public CurrentUser provide(User user, UserTenant userTenant, Role role) {
    return new CurrentUser(user, userTenant, role);
  }
}
