package com.sourcefuse.jarc.services.authservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.payload.*;
import com.sourcefuse.jarc.services.authservice.providers.ClientPasswordVerifyProvider;
import com.sourcefuse.jarc.services.authservice.providers.ResourceOwnerVerifyProvider;
import com.sourcefuse.jarc.services.authservice.services.AuthService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;
  private final ClientPasswordVerifyProvider clientPasswordVerifyProvider;
  private final ResourceOwnerVerifyProvider resourceOwnerVerifyProvider;

  @PostMapping("/token")
  public JWTAuthResponse getTokenByCode(@RequestBody AuthTokenRequest authTokenRequest) {
    return this.authService.getTokenByCode(authTokenRequest);
  }

  @PostMapping(value = { "/login" })
  public ResponseEntity<CodeResponse> login(@RequestBody LoginDto loginDto) {
    AuthClient client = this.clientPasswordVerifyProvider.value(
        loginDto.getClient_id(),
        loginDto.getClient_secret());
    UserVerificationDTO userVerificationDTO = this.resourceOwnerVerifyProvider.provide(loginDto);

    String code = authService.login(loginDto, client, userVerificationDTO.getAuthUser());

    CodeResponse codeResponse = new CodeResponse();
    codeResponse.setCode(code);

    return ResponseEntity.ok(codeResponse);
  }
}
