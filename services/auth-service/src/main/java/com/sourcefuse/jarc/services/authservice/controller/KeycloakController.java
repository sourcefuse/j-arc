package com.sourcefuse.jarc.services.authservice.controller;

import com.sourcefuse.jarc.services.authservice.dtos.ClientDTO;
import com.sourcefuse.jarc.services.authservice.dtos.CodeResponse;
import com.sourcefuse.jarc.services.authservice.enums.AuthErrorKeys;
import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.repositories.AuthClientRepository;
import com.sourcefuse.jarc.services.authservice.services.KeycloakAuthService;
import com.sourcefuse.jarc.services.authservice.specifications.AuthClientSpecification;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

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
  public void authRedirectCallback(
    @Valid @RequestParam("code") String code,
    @Valid @RequestParam("state") String state,
    HttpServletResponse httpServletResponse
  ) {
    String authClientId = state.split("=")[1];
    AuthClient authClient = authClientRepository
      .findOne(AuthClientSpecification.byClientId(authClientId))
      .orElseThrow(() ->
        new HttpServerErrorException(
          HttpStatus.UNAUTHORIZED,
          AuthErrorKeys.CLIENT_INVALID.toString()
        )
      );
    CodeResponse codeResponse =
      this.keycloakAuthService.login(code, authClient);
    httpServletResponse.setHeader(
      "Location",
      java.text.MessageFormat.format(
        "{0}?code={1}",
        authClient.getRedirectUrl(),
        codeResponse.getCode()
      )
    );
    httpServletResponse.setStatus(HttpStatus.FOUND.value());
  }

  @PostMapping("/login")
  public void keycloak(
    HttpServletResponse httpServletResponse,
    @Valid @ModelAttribute ClientDTO clientDTO
  ) {
    String redirectUrlWithParam = java.text.MessageFormat.format(
      "{0}&state=auth_client_id={1}",
      redirectUrl,
      clientDTO.getClientId()
    );

    String location =
      keycloakUrl +
      "?response_type=code&client_id=" +
      clientId +
      "&scope=openid&redirect_uri=" +
      redirectUrlWithParam +
      "&auth_client_id=" +
      clientDTO.getClientId();
    httpServletResponse.setHeader("Location", location);
    httpServletResponse.setStatus(HttpStatus.FOUND.value());
  }
}
