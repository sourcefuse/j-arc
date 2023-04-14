package com.sourcefuse.jarc.services.authservice.providers;

import org.springframework.stereotype.Service;
import com.sourcefuse.jarc.services.authservice.models.User;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AuthCodeGeneratorProvider {

  private final CodeWriterProvider codeWriterProvider;
  private final JwtTokenProvider jwtTokenProvider;

  public String provide(User user) {
    String token = this.jwtTokenProvider.generateToken(user);
    return String.valueOf(this.codeWriterProvider.provide(token));
  }
}
