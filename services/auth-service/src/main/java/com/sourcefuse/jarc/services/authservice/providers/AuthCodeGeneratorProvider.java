package com.sourcefuse.jarc.services.authservice.providers;

import org.springframework.stereotype.Service;

import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.payload.CodeResponse;
import com.sourcefuse.jarc.services.authservice.payload.JWTAuthResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AuthCodeGeneratorProvider {

  private final CodeWriterProvider codeWriterProvider;
  private final JwtTokenProvider jwtTokenProvider;

  public CodeResponse provide(User user, AuthClient authClient) {
    JWTAuthResponse jwtAuthResponse = this.jwtTokenProvider.createJwt(user, authClient);
    String code = String.valueOf(this.codeWriterProvider.provide(jwtAuthResponse.getAccessToken()));
    CodeResponse codeResponse = new CodeResponse();
    codeResponse.setCode(code);

    return codeResponse;
  }
}
