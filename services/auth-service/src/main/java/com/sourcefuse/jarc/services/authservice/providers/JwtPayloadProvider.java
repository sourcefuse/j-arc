package com.sourcefuse.jarc.services.authservice.providers;

import org.springframework.stereotype.Service;

import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.session.CurrentUser;

@Service
public class JwtPayloadProvider {

  public CurrentUser provide(User user) {
    return new CurrentUser(user);
  }
}
