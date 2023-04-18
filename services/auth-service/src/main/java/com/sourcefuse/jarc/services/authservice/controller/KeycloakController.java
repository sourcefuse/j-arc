package com.sourcefuse.jarc.services.authservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import com.sourcefuse.jarc.services.authservice.enums.AuthErrorKeys;
import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.payload.ClientDTO;
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
      @RequestParam("state") String state,
      HttpServletResponse httpServletResponse) {
    String authClientId = state.split("=")[1];
    AuthClient authClient = authClientRepository.findAuthClientByClientId(authClientId)
        .orElseThrow(() -> new HttpServerErrorException(
            HttpStatus.UNAUTHORIZED,
            AuthErrorKeys.ClientInvalid.label));
    CodeResponse codeResponse = this.keycloakAuthService.login(code, authClient);
    httpServletResponse.setHeader(
        "Location",
        java.text.MessageFormat.format("{0}?code={1}", authClient.getRedirectUrl(), codeResponse.getCode()));
    httpServletResponse.setStatus(302);
  }

  @PostMapping("/login")
  public void keycloak(HttpServletResponse httpServletResponse,
      @RequestBody ClientDTO clientDTO) {
    String redirectUrlWithParam = java.text.MessageFormat.format("{0}&state=auth_client_id={1}",
        redirectUrl,
        clientDTO.getClientId());
    String url = "{0}?response_type=code&client_id={1}&scope=openid&redirect_uri={2}&auth_client_id={3}";
    String location = java.text.MessageFormat.format(url, keycloakUrl, clientId, redirectUrlWithParam,
        clientDTO.getClientId());
    httpServletResponse.setHeader(
        "Location", location);
    httpServletResponse.setStatus(302);
  }
}
