package com.sourcefuse.jarc.services.authservice.providers;

import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.session.CurrentUser;
import org.springframework.stereotype.Service;

@Service
public class JwtPayloadProvider {

  public CurrentUser provide(User user) {
    return new CurrentUser(user);
  }
}
