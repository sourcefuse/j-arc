package com.sourcefuse.jarc.services.authservice.providers;

import com.sourcefuse.jarc.services.authservice.dtos.CodeResponse;
import com.sourcefuse.jarc.services.authservice.dtos.JWTAuthResponse;
import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.models.Role;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthCodeGeneratorProvider {

  private final CodeWriterProvider codeWriterProvider;
  private final JwtTokenProvider jwtTokenProvider;

  public CodeResponse provide(
      User user,
      UserTenant userTenant,
      Role role,
      AuthClient authClient) {
    JWTAuthResponse jwtAuthResponse = this.jwtTokenProvider.createJwt(user, userTenant, role, authClient);
    String code = String.valueOf(
        this.codeWriterProvider.provide(jwtAuthResponse.getAccessToken()));
    CodeResponse codeResponse = new CodeResponse();
    codeResponse.setCode(code);

    return codeResponse;
  }
}
