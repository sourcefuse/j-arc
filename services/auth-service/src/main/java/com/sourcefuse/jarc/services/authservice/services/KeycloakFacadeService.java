package com.sourcefuse.jarc.services.authservice.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.sourcefuse.jarc.services.authservice.payload.keycloak.KeycloakAuthResponse;
import com.sourcefuse.jarc.services.authservice.payload.keycloak.KeycloakUserDTO;

@Component
public class KeycloakFacadeService {

  @Value("${app.keycloak.token-endpoint}")
  private String keycloakTokenEndpoint;
  @Value("${app.keycloak.user-info-endpoint}")
  private String keycloakUserInfoEndpoint;
  @Value("${app.keycloak.redirect-url}")
  private String redirectUrl;

  @Value("${app.keycloak.client-id}")
  private String clientId;

  @Value("${app.keycloak.client-secret}")
  private String clientSecret;
  @Value("${app.keycloak.keycloak-url}")
  private String keycloakUrl;


  public KeycloakAuthResponse keycloakAuthByCode(String code) {

    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", "*/*");
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
    map.add("client_id", clientId);
    map.add("client_secret", clientSecret);
    map.add("code", code);
    map.add("redirect_uri", redirectUrl);
    map.add("grant_type", "authorization_code");

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<KeycloakAuthResponse> response = restTemplate.postForEntity(
        keycloakTokenEndpoint,
        request,
        KeycloakAuthResponse.class);
    return response.getBody();
  }

  public KeycloakUserDTO getKeycloakUserProfile(String accessToken) {

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity request = new HttpEntity(headers);
    ResponseEntity<KeycloakUserDTO> response = restTemplate.exchange(
            keycloakUserInfoEndpoint,
        HttpMethod.GET,
        request,
        KeycloakUserDTO.class);
    return response.getBody();
  }
}
