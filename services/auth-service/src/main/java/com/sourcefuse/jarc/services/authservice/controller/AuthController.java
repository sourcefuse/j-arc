package com.sourcefuse.jarc.services.authservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.payload.AuthTokenRequest;
import com.sourcefuse.jarc.services.authservice.payload.CodeResponse;
import com.sourcefuse.jarc.services.authservice.payload.JWTAuthResponse;
import com.sourcefuse.jarc.services.authservice.payload.LoginDto;
import com.sourcefuse.jarc.services.authservice.payload.RefreshTokenDTO;
import com.sourcefuse.jarc.services.authservice.payload.UserVerificationDTO;
import com.sourcefuse.jarc.services.authservice.providers.ClientPasswordVerifyProvider;
import com.sourcefuse.jarc.services.authservice.providers.ResourceOwnerVerifyProvider;
import com.sourcefuse.jarc.services.authservice.services.AuthService;
import com.sourcefuse.jarc.services.authservice.session.CurrentUser;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;
  private final ClientPasswordVerifyProvider clientPasswordVerifyProvider;
  private final ResourceOwnerVerifyProvider resourceOwnerVerifyProvider;

  @PostMapping(value = { "/login" })
  public ResponseEntity<CodeResponse> login(@RequestBody LoginDto loginDto) {
    AuthClient client = this.clientPasswordVerifyProvider.value(
        loginDto.getClientId(),
        loginDto.getClientSecret());
    UserVerificationDTO userVerificationDTO = this.resourceOwnerVerifyProvider.provide(loginDto);

    CodeResponse codeResponse = authService.login(loginDto, client, userVerificationDTO.getAuthUser());
    return ResponseEntity.ok(codeResponse);
  }

  @PostMapping(value = { "/login-token" })
  public ResponseEntity<JWTAuthResponse> loginToken(@RequestBody LoginDto loginDto) {
    AuthClient client = this.clientPasswordVerifyProvider.value(
        loginDto.getClientId(),
        loginDto.getClientSecret());
    UserVerificationDTO userVerificationDTO = this.resourceOwnerVerifyProvider.provide(loginDto);
    JWTAuthResponse jwtAuthResponse = authService.loginToken(loginDto, client, userVerificationDTO.getAuthUser());
    return ResponseEntity.ok(jwtAuthResponse);
  }

  @PostMapping("/token-refresh")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<JWTAuthResponse> exchangeToken(@RequestBody RefreshTokenDTO refreshTokenDTO,
      @RequestHeader("Authorization") String authorizationHeader) {
    JWTAuthResponse jwtAuthResponse = authService.refreshToken(authorizationHeader, refreshTokenDTO);
    return ResponseEntity.ok(jwtAuthResponse);
  }

  @PostMapping("/token")
  public JWTAuthResponse getTokenByCode(@RequestBody AuthTokenRequest authTokenRequest) {
    return this.authService.getTokenByCode(authTokenRequest);
  }

  @GetMapping("/me")
  @PreAuthorize("isAuthenticated()")
  public CurrentUser getAuthenticatedCurrentUser() {
    return (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}