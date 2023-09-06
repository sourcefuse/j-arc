package com.sourcefuse.jarc.services.authservice.oauth2.user;

import java.util.Map;

public class GithubOAuth2UserInfo extends OAuth2UserInfo {

  public GithubOAuth2UserInfo(Map<String, Object> attributes) {
    super(attributes);
  }

  @Override
  public String getId() {
    return ((Integer) attributes.get("id")).toString();
  }

  @Override
  public String getName() {
    String name = (String) attributes.get("name");
    return name != null && !name.isBlank() ? name : getLogin();
  }

  @Override
  public String getEmail() {
    String email = (String) attributes.get("email");
    return email != null && !email.isBlank() ? email : getLogin();
  }

  @Override
  public String getImageUrl() {
    return (String) attributes.get("avatar_url");
  }

  public String getLogin() {
    return (String) attributes.get("login");
  }
}
