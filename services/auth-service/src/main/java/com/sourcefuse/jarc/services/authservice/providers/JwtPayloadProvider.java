package com.sourcefuse.jarc.services.authservice.providers;

import org.springframework.stereotype.Service;
import com.sourcefuse.jarc.services.authservice.models.User;

@Service
public class JwtPayloadProvider {

  public User provide(User user) {
    return user;
  }
}
