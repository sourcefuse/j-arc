package com.sourcefuse.jarc.services.authservice.oauth2.services;

import com.sourcefuse.jarc.services.authservice.enums.AuthProvider;
import com.sourcefuse.jarc.services.authservice.models.Role;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserCredential;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import com.sourcefuse.jarc.services.authservice.oauth2.auth.utils.CommonUtils;
import com.sourcefuse.jarc.services.authservice.oauth2.providers.OAuth2PreVerifyProvider;
import com.sourcefuse.jarc.services.authservice.oauth2.providers.OAuth2SignupProvider;
import com.sourcefuse.jarc.services.authservice.oauth2.user.OAuth2UserInfo;
import com.sourcefuse.jarc.services.authservice.oauth2.user.session.OAuth2UserSession;
import com.sourcefuse.jarc.services.authservice.repositories.RoleRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserCredentialRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserTenantRepository;
import com.sourcefuse.jarc.services.authservice.specifications.UserCredentialSpecification;
import com.sourcefuse.jarc.services.authservice.specifications.UserSpecification;
import com.sourcefuse.jarc.services.authservice.specifications.UserTenantSpecification;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;

  private final UserCredentialRepository userCredentialRepository;
  private final RoleRepository roleRepository;
  private final UserTenantRepository userTenantRepository;

  private final OAuth2SignupProvider oAuth2SignupProvider;
  private final OAuth2PreVerifyProvider oAuth2PreVerifyProvider;

  @Override
  @Transactional
  public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest)
    throws OAuth2AuthenticationException {
    try {
      OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
      return processOAuth2User(oAuth2UserRequest, oAuth2User);
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

  public OAuth2User processOAuth2User(
    OAuth2UserRequest oAuth2UserRequest,
    OAuth2User oAuth2User
  ) {
    String provider = oAuth2UserRequest
      .getClientRegistration()
      .getRegistrationId();
    OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoService.getOAuth2UserInfo(
      oAuth2UserRequest.getClientRegistration().getRegistrationId(),
      oAuth2User.getAttributes()
    );

    this.oAuth2SignupProvider.verifyOAuth2UserEmail(oAuth2UserInfo.getEmail());

    Optional<User> userOptional =
      this.userRepository.findOne(
          UserSpecification.byUserNameOrEmail(oAuth2UserInfo.getEmail())
        );
    User user;
    if (userOptional.isPresent()) {
      user =
        this.oAuth2PreVerifyProvider.provide(
            userOptional.get(),
            oAuth2UserInfo
          );
    } else {
      user =
        this.oAuth2SignupProvider.provide(
            oAuth2UserInfo,
            AuthProvider.valueOf(provider.toUpperCase())
          );
    }
    UserCredential userCredential =
      this.userCredentialRepository.findOne(
          UserCredentialSpecification.byUserId(user.getId())
        )
        .orElseThrow(CommonUtils::throwUserVerificationFailed);
    if (
      !userCredential.getAuthProvider().equalsIgnoreCase(provider) ||
      (!userCredential.getAuthId().equalsIgnoreCase(oAuth2UserInfo.getId()))
    ) {
      throw CommonUtils.throwUserVerificationFailed();
    }
    UserTenant userTenant =
      this.userTenantRepository.findOne(
          UserTenantSpecification.byUserId(user.getId())
        )
        .orElseThrow(CommonUtils::throwUserVerificationFailed);

    Role role =
      this.roleRepository.findById(userTenant.getRoleId())
        .orElseThrow(CommonUtils::throwUserVerificationFailed);

    return new OAuth2UserSession(user, userTenant, role, oAuth2User);
  }
}
