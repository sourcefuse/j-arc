package com.sourcefuse.jarc.services.authservice.controller;

import com.sourcefuse.jarc.services.authservice.dtos.AuthTokenRequest;
import com.sourcefuse.jarc.services.authservice.dtos.CodeResponse;
import com.sourcefuse.jarc.services.authservice.dtos.JWTAuthResponse;
import com.sourcefuse.jarc.services.authservice.dtos.LoginDto;
import com.sourcefuse.jarc.services.authservice.dtos.RefreshTokenDTO;
import com.sourcefuse.jarc.services.authservice.dtos.UserVerificationDTO;
import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.providers.ClientPasswordVerifyProvider;
import com.sourcefuse.jarc.services.authservice.providers.ResourceOwnerVerifyProvider;
import com.sourcefuse.jarc.services.authservice.services.AuthService;
import com.sourcefuse.jarc.services.authservice.services.JwtService;
import com.sourcefuse.jarc.services.authservice.session.CurrentUser;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;
  private final JwtService jwtService;
  private final ClientPasswordVerifyProvider clientPasswordVerifyProvider;
  private final ResourceOwnerVerifyProvider resourceOwnerVerifyProvider;

  @PostMapping(value = { "/login" })
  public ResponseEntity<CodeResponse> login(@RequestBody LoginDto loginDto) {
    AuthClient client =
      this.clientPasswordVerifyProvider.value(
          loginDto.getClientId(),
          loginDto.getClientSecret()
        );
    UserVerificationDTO userVerificationDTO =
      this.resourceOwnerVerifyProvider.provide(loginDto);

    CodeResponse codeResponse = authService.login(
      loginDto,
      client,
      userVerificationDTO.getAuthUser()
    );
    return ResponseEntity.ok(codeResponse);
  }

  @PostMapping(value = { "/login-token" })
  public ResponseEntity<JWTAuthResponse> loginToken(
    @RequestBody LoginDto loginDto
  ) {
    AuthClient client =
      this.clientPasswordVerifyProvider.value(
          loginDto.getClientId(),
          loginDto.getClientSecret()
        );
    UserVerificationDTO userVerificationDTO =
      this.resourceOwnerVerifyProvider.provide(loginDto);
    JWTAuthResponse jwtAuthResponse = authService.loginToken(
      loginDto,
      client,
      userVerificationDTO.getAuthUser()
    );
    return ResponseEntity.ok(jwtAuthResponse);
  }

  @PostMapping("/token-refresh")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<JWTAuthResponse> exchangeToken(
    @RequestBody RefreshTokenDTO refreshTokenDTO,
    @RequestHeader("Authorization") String authorizationHeader
  ) {
    JWTAuthResponse jwtAuthResponse = jwtService.refreshToken(
      authorizationHeader,
      refreshTokenDTO
    );
    return ResponseEntity.ok(jwtAuthResponse);
  }

  @PostMapping("/token")
  public JWTAuthResponse getTokenByCode(
    @RequestBody AuthTokenRequest authTokenRequest
  ) {
    return this.jwtService.getTokenByCode(authTokenRequest);
  }

  @GetMapping("/me")
  @PreAuthorize("isAuthenticated()")
  public CurrentUser getAuthenticatedCurrentUser() {
    return (CurrentUser) SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getPrincipal();
  }
}
