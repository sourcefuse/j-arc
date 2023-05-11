package com.sourcefuse.jarc.services.authservice.payload.keycloak;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakUserDTO {

  private String sub;
  @JsonProperty("email_verified")
  private boolean emailVerified;
  private String name;
  @JsonProperty("preferred_username")
  private String preferredUsername;
  @JsonProperty("given_name")
  private String givenName;
  @JsonProperty("family_name")
  private String familyName;
  private String email;
}
