package com.sourcefuse.jarc.services.authservice.oauth2.services;

import com.sourcefuse.jarc.services.authservice.enums.AuthErrorKeys;
import com.sourcefuse.jarc.services.authservice.enums.AuthProvider;
import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.models.Role;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserCredential;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import com.sourcefuse.jarc.services.authservice.oauth2.providers.OAuth2PreVerifyProvider;
import com.sourcefuse.jarc.services.authservice.oauth2.providers.OAuth2SignupProvider;
import com.sourcefuse.jarc.services.authservice.oauth2.user.session.OAuth2UserSession;
import com.sourcefuse.jarc.services.authservice.repositories.AuthClientRepository;
import com.sourcefuse.jarc.services.authservice.repositories.RoleRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserCredentialRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserTenantRepository;
import com.sourcefuse.jarc.services.authservice.specifications.AuthClientSpecification;
import com.sourcefuse.jarc.services.authservice.specifications.UserCredentialSpecification;
import com.sourcefuse.jarc.services.authservice.specifications.UserSpecification;
import com.sourcefuse.jarc.services.authservice.specifications.UserTenantSpecification;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

  private final UserRepository userRepository;

  private final UserCredentialRepository userCredentialRepository;
  private final RoleRepository roleRepository;
  private final UserTenantRepository userTenantRepository;
  private final AuthClientRepository authClientRepository;

  private final OAuth2SignupProvider oAuth2SignupProvider;
  private final OAuth2PreVerifyProvider oAuth2PreVerifyProvider;

  @Override
  @Transactional
  public OidcUser loadUser(OidcUserRequest oidcUserRequest)
    throws OAuth2AuthenticationException {
    OidcUser oidcUser = super.loadUser(oidcUserRequest);

    try {
      return processOAuth2User(oidcUserRequest, oidcUser);
    } catch (AuthenticationException ex) {
      log.error(null, ex);
      throw ex;
    } catch (Exception ex) {
      log.error(null, ex);
      // Throwing an instance of AuthenticationException will trigger the
      // OAuth2AuthenticationFailureHandler
      throw new InternalAuthenticationServiceException(
        ex.getMessage(),
        ex.getCause()
      );
    }
  }

  private OidcUser processOAuth2User(
    OidcUserRequest oidcUserRequest,
    OidcUser oidcUser
  ) {
    this.oAuth2SignupProvider.verifyOAuth2UserEmail(oidcUser.getEmail());

    Optional<User> userOptional =
      this.userRepository.findOne(
          UserSpecification.byUserNameOrEmail(oidcUser.getEmail())
        );

    String provider = oidcUserRequest
      .getClientRegistration()
      .getRegistrationId();
    User user;
    if (userOptional.isPresent()) {
      user = this.oAuth2PreVerifyProvider.provide(userOptional.get(), oidcUser);
    } else {
      user =
        this.oAuth2SignupProvider.provide(
            oidcUser,
            AuthProvider.valueOf(provider.toUpperCase())
          );
    }
    UserCredential userCredential =
      this.userCredentialRepository.findOne(
          UserCredentialSpecification.byUserId(user.getId())
        )
        .orElseThrow(this::throwUserVerificationFailed);
    if (
      !userCredential.getAuthProvider().equalsIgnoreCase(provider) ||
      (!userCredential
          .getAuthId()
          .equalsIgnoreCase(oidcUser.getPreferredUsername()))
    ) {
      throw throwUserVerificationFailed();
    }
    UserTenant userTenant =
      this.userTenantRepository.findOne(
          UserTenantSpecification.byUserId(user.getId())
        )
        .orElseThrow(this::throwUserVerificationFailed);

    Role role =
      this.roleRepository.findById(userTenant.getRoleId())
        .orElseThrow(this::throwUserVerificationFailed);

    AuthClient authClient = authClientRepository
      .findOne(
        AuthClientSpecification.byClientId(
          oidcUserRequest.getClientRegistration().getClientId()
        )
      )
      .orElseThrow(this::throwUserVerificationFailed);

    return new OAuth2UserSession(user, userTenant, role, authClient, oidcUser);
  }

  private OAuth2AuthenticationException throwUserVerificationFailed() {
    return new OAuth2AuthenticationException(
      AuthErrorKeys.USER_VERIFICATION_FAILED.toString()
    );
  }
}
