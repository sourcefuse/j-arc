package com.sourcefuse.jarc.services.authservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import com.sourcefuse.jarc.services.authservice.enums.AuthErrorKeys;
import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.payload.CodeResponse;
import com.sourcefuse.jarc.services.authservice.repositories.AuthClientRepository;
import com.sourcefuse.jarc.services.authservice.services.KeycloakAuthService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/keycloak")
public class KeycloakController {

  @Value("${app.keycloak.redirect-url}")
  String redirectUrl;
  @Value("${app.keycloak.client-id}")
  String clientId;
  @Value("${app.keycloak.keycloak-url}")
  String keycloakUrl;

  private final KeycloakAuthService keycloakAuthService;

  private final AuthClientRepository authClientRepository;

  @GetMapping("/auth-redirect-callback")
  public void authRedirectCallback(@RequestParam("code") String code,
      HttpServletResponse httpServletResponse) {
    // TODO
    String clientId = "pms_webapp";
    AuthClient authClient = authClientRepository.findAuthClientByClientId(clientId)
        .orElseThrow(() -> new HttpServerErrorException(
            HttpStatus.UNAUTHORIZED,
            AuthErrorKeys.ClientInvalid.label));
    CodeResponse codeResponse = this.keycloakAuthService.login(code, authClient);
    httpServletResponse.setHeader(
        "Location",
        java.text.MessageFormat.format("{0}?code={1}", redirectUrl, codeResponse.getCode()));
    httpServletResponse.setStatus(302);
  }

  @GetMapping("/login")
  public void keycloak(HttpServletResponse httpServletResponse) {
    String url = "{0}?response_type=code&client_id={1}&scope=openid&redirect_uri={2}";
    httpServletResponse.setHeader(
        "Location",
        java.text.MessageFormat.format(url, keycloakUrl, clientId, redirectUrl));
    httpServletResponse.setStatus(302);
  }
}
