package com.sourcefuse.jarc.services.authservice.oauth2.user.session;

import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.models.Role;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class OAuth2UserSession implements OidcUser {

  private User user;
  private UserTenant userTenant;
  private Role role;
  private AuthClient authClient;

  Map<String, Object> attributes;

  OidcUserInfo userInfo;
  OidcIdToken idToken;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.role.getPermissions()
      .stream()
      .map(permission -> new SimpleGrantedAuthority(permission))
      .toList();
  }

  @Override
  public String getName() {
    return String.join(
      " ",
      Arrays
        .asList(
          this.user.getFirstName(),
          this.user.getMiddleName(),
          this.user.getLastName()
        )
        .stream()
        .filter(ele -> ele != null)
        .toList()
    );
  }

  @Override
  public Map<String, Object> getClaims() {
    return this.getAttributes();
  }

  public OAuth2UserSession(
    User user,
    UserTenant userTenant,
    Role role,
    AuthClient authClient,
    OAuth2User oAuth2User
  ) {
    this.user = user;
    this.userTenant = userTenant;
    this.role = role;
    this.authClient = authClient;
    this.attributes = oAuth2User.getAttributes();
  }

  public OAuth2UserSession(
    User user,
    UserTenant userTenant,
    Role role,
    AuthClient authClient,
    OidcUser oidcUser
  ) {
    this.user = user;
    this.userTenant = userTenant;
    this.role = role;
    this.authClient = authClient;
    this.attributes = oidcUser.getAttributes();
    this.userInfo = oidcUser.getUserInfo();
    this.idToken = oidcUser.getIdToken();
  }
}
