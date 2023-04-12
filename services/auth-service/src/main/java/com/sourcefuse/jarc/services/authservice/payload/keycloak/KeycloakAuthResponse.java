package com.sourcefuse.jarc.services.authservice.payload.keycloak;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakAuthResponse {

  private String access_token;
  private int expires_in;
  private int refresh_expires_in;
  private String refresh_token;
  private String token_type;
  private String id_token;
  // public int not-before-policy;
  private String session_state;
  private String scope;
}
