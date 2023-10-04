package com.sourcefuse.jarc.services.authservice.tests.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sourcefuse.jarc.services.authservice.enums.AuthProvider;
import com.sourcefuse.jarc.services.authservice.models.Role;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserCredential;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import com.sourcefuse.jarc.services.authservice.oauth2.providers.OAuth2PreVerifyProvider;
import com.sourcefuse.jarc.services.authservice.oauth2.providers.OAuth2SignupProvider;
import com.sourcefuse.jarc.services.authservice.oauth2.services.CustomOAuth2UserService;
import com.sourcefuse.jarc.services.authservice.oauth2.user.OAuth2UserInfo;
import com.sourcefuse.jarc.services.authservice.repositories.RoleRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserCredentialRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserTenantRepository;
import com.sourcefuse.jarc.services.authservice.tests.mocks.MockObjects;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

class CustomOAuth2UserServiceTests2 {

  @Mock
  UserRepository userRepository;

  @Mock
  UserCredentialRepository userCredentialRepository;

  @Mock
  RoleRepository roleRepository;

  @Mock
  UserTenantRepository userTenantRepository;

  @Mock
  OAuth2SignupProvider oAuth2SignupProvider;

  @Mock
  OAuth2PreVerifyProvider oAuth2PreVerifyProvider;

  @InjectMocks
  CustomOAuth2UserService customOAuth2UserService;

  OAuth2User oAuth2User;
  OAuth2UserRequest oAuth2UserRequest;

  @SuppressWarnings("unchecked")
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    oAuth2User = MockObjects.createMockOAuth2User();
    oAuth2UserRequest = MockObjects.createMockOAuth2UserRequest();

    User user = MockObjects.createMockUser();
    Mockito
      .when(user.getId())
      .thenReturn(UUID.fromString("878387a0-53ec-409f-bc60-51838afb45ce"));
    Mockito
      .when(userRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.of(user));

    UserTenant userTenant = MockObjects.createMockUserTenant();
    Mockito
      .when(userTenantRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.of(userTenant));

    UserCredential userCredential = MockObjects.createMockUserCredentials(
      MockObjects.USER_USERNAME,
      MockObjects.CLIENT_ID
    );
    Mockito
      .when(userCredentialRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.of(userCredential));

    Mockito
      .when(roleRepository.findById(Mockito.any(UUID.class)))
      .thenReturn(Optional.of(Mockito.mock(Role.class)));

    Mockito
      .when(
        oAuth2PreVerifyProvider.provide(
          Mockito.any(User.class),
          Mockito.any(OAuth2UserInfo.class)
        )
      )
      .thenReturn(user);

    Mockito
      .when(
        oAuth2SignupProvider.provide(
          Mockito.any(OAuth2UserInfo.class),
          Mockito.any(AuthProvider.class)
        )
      )
      .thenReturn(user);
  }

  @Test
  void testSuccessWithUserFoundInDB() {
    customOAuth2UserService.processOAuth2User(oAuth2UserRequest, oAuth2User);
    Mockito
      .verify(oAuth2PreVerifyProvider, Mockito.times(1))
      .provide(Mockito.any(User.class), Mockito.any(OAuth2UserInfo.class));
  }

  @SuppressWarnings("unchecked")
  @Test
  void testSuccessWithUserNotFoundInDB() {
    Mockito
      .when(userRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.ofNullable(null));
    customOAuth2UserService.processOAuth2User(oAuth2UserRequest, oAuth2User);
    Mockito
      .verify(oAuth2SignupProvider, Mockito.times(1))
      .provide(
        Mockito.any(OAuth2UserInfo.class),
        Mockito.any(AuthProvider.class)
      );
  }

  @SuppressWarnings("unchecked")
  @Test
  void testProcessOAuth2UserFailedDueToUserCredentialsNotFound() {
    Mockito
      .when(userCredentialRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.ofNullable(null));
    assertThrows(
      OAuth2AuthenticationException.class,
      () ->
        customOAuth2UserService.processOAuth2User(oAuth2UserRequest, oAuth2User)
    );
  }

  @SuppressWarnings("unchecked")
  @Test
  void testProcessOAuth2UserFailedDueToUserCredentialsAuthIdNotMatches() {
    UserCredential userCredential = MockObjects.createMockUserCredentials(
      MockObjects.USER_EMAIL,
      MockObjects.CLIENT_ID
    );
    Mockito
      .when(userCredentialRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.of(userCredential));
    assertThrows(
      OAuth2AuthenticationException.class,
      () ->
        customOAuth2UserService.processOAuth2User(oAuth2UserRequest, oAuth2User)
    );
  }

  @SuppressWarnings("unchecked")
  @Test
  void testProcessOAuth2UserFailedDueToUserCredentialsAuthProviderNotMatches() {
    UserCredential userCredential = MockObjects.createMockUserCredentials(
      MockObjects.USER_USERNAME,
      AuthProvider.GITHUB.toString()
    );
    Mockito
      .when(userCredentialRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.of(userCredential));

    assertThrows(
      OAuth2AuthenticationException.class,
      () ->
        customOAuth2UserService.processOAuth2User(oAuth2UserRequest, oAuth2User)
    );
  }

  @SuppressWarnings("unchecked")
  @Test
  void testProcessOAuth2UserFailedDueToUserTenantNotFound() {
    Mockito
      .when(userTenantRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.ofNullable(null));
    assertThrows(
      OAuth2AuthenticationException.class,
      () ->
        customOAuth2UserService.processOAuth2User(oAuth2UserRequest, oAuth2User)
    );
  }

  @Test
  void testProcessOAuth2UserFailedDueToRoleNotFound() {
    Mockito
      .when(roleRepository.findById(Mockito.any(UUID.class)))
      .thenReturn(Optional.ofNullable(null));
    assertThrows(
      OAuth2AuthenticationException.class,
      () ->
        customOAuth2UserService.processOAuth2User(oAuth2UserRequest, oAuth2User)
    );
  }

  @Test
  void testLoaduserFailsDueToSuperClassMethodInvocationFailed() {
    assertThrows(
      OAuth2AuthenticationException.class,
      () -> customOAuth2UserService.loadUser(oAuth2UserRequest)
    );
  }
}
