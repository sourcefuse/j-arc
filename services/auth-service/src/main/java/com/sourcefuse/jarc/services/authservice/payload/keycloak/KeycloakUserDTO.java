package com.sourcefuse.jarc.services.authservice.payload.keycloak;

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
  private boolean email_verified;
  private String name;
  private String preferred_username;
  private String given_name;
  private String family_name;
  private String email;
}
