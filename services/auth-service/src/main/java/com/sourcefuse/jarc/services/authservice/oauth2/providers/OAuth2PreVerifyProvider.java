package com.sourcefuse.jarc.services.authservice.oauth2.providers;

import com.sourcefuse.jarc.core.enums.UserStatus;
import com.sourcefuse.jarc.services.authservice.enums.AuthErrorKeys;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import com.sourcefuse.jarc.services.authservice.oauth2.user.OAuth2UserInfo;
import com.sourcefuse.jarc.services.authservice.repositories.UserRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserTenantRepository;
import com.sourcefuse.jarc.services.authservice.specifications.UserTenantSpecification;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@AllArgsConstructor
@Service
public class OAuth2PreVerifyProvider {

  private final UserRepository userRepository;
  private final UserTenantRepository userTenantRepository;

  public User provide(User user, OidcUser oidcUser) {
    if (
      !user.getFirstName().equals(oidcUser.getGivenName()) ||
      !user.getLastName().equals(oidcUser.getFamilyName()) ||
      !user.getUsername().equals(oidcUser.getPreferredUsername()) ||
      !user.getEmail().equals(oidcUser.getEmail())
    ) {
      user.setUsername(oidcUser.getPreferredUsername());
      user.setFirstName(oidcUser.getGivenName());
      user.setLastName(oidcUser.getFamilyName());
      user.setEmail(oidcUser.getEmail());
      this.userRepository.save(user);
    }
    checkUserTenant(user);
    return user;
  }

  public User provide(User user, OAuth2UserInfo oAuth2UserInfo) {
    String firstName = oAuth2UserInfo.getName().split(" ")[0];
    String lastName = oAuth2UserInfo.getName().split(" ")[1];
    if (
      (firstName != null && !user.getFirstName().equals(firstName)) ||
      (lastName != null && !user.getLastName().equals(lastName)) ||
      !user.getUsername().equals(oAuth2UserInfo.getEmail()) ||
      !user.getEmail().equals(oAuth2UserInfo.getEmail())
    ) {
      user.setUsername(oAuth2UserInfo.getEmail());
      user.setFirstName(firstName);
      user.setLastName(lastName);
      user.setEmail(oAuth2UserInfo.getEmail());
      this.userRepository.save(user);
    }
    checkUserTenant(user);
    return user;
  }

  private void checkUserTenant(User user) {
    Optional<UserTenant> userTenant =
      this.userTenantRepository.findOne(
          UserTenantSpecification.byUserId(user.getId())
        );
    if (userTenant.isEmpty()) {
      throw new HttpServerErrorException(
        HttpStatus.UNAUTHORIZED,
        AuthErrorKeys.INVALID_CREDENTIALS.toString()
      );
    }
    if (userTenant.get().getStatus() == UserStatus.REGISTERED) {
      userTenant.get().setStatus(UserStatus.ACTIVE);
      this.userTenantRepository.save(userTenant.get());
    }
  }
}
