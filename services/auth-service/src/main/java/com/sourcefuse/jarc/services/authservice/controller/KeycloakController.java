package com.sourcefuse.jarc.services.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sourcefuse.jarc.services.authservice.services.KeycloakAuthService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

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

  @GetMapping("/auth-redirect-callback")
  public String authRedirectCallback(@RequestParam("code") String code) {
    return this.keycloakAuthService.login(code);
  }

  @GetMapping("/login")
  public void keycloak(HttpServletResponse httpServletResponse) {
    String url="{0}?response_type=code&client_id={1}&scope=openid&redirect_uri={2}";
    httpServletResponse.setHeader(
        "Location",
            java.text.MessageFormat.format(url,keycloakUrl,clientId,redirectUrl));
    httpServletResponse.setStatus(302);
  }
}
