package com.sourcefuse.jarc.services.authservice.enums;

public enum AuthProvider {
  INTERNAL("internal"),
  KEYCLOAK("keycloak"),
  GOOGLE("google"),
  FACEBOOK("facebook"),
  APPLE("apple"),
  INSTAGRAM("instagram");

  public final String label;

  private AuthProvider(String label) {
    this.label = label;
  }
}
