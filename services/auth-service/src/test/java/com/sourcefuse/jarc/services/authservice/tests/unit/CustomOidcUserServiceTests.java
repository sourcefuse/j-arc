package com.sourcefuse.jarc.services.authservice.tests.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sourcefuse.jarc.services.authservice.enums.AuthProvider;
import com.sourcefuse.jarc.services.authservice.models.Role;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserCredential;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import com.sourcefuse.jarc.services.authservice.oauth2.providers.OAuth2PreVerifyProvider;
import com.sourcefuse.jarc.services.authservice.oauth2.providers.OAuth2SignupProvider;
import com.sourcefuse.jarc.services.authservice.oauth2.services.CustomOidcUserService;
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
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

class CustomOidcUserServiceTests {

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
  CustomOidcUserService customOidcUserService;

  OidcUser oidcUser;
  OidcUserRequest oidcUserRequest;

  @SuppressWarnings("unchecked")
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    oidcUser = MockObjects.createMockOidcUser();
    oidcUserRequest = MockObjects.createMockOidcUserRequest();

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
          Mockito.any(OidcUser.class)
        )
      )
      .thenReturn(user);

    Mockito
      .when(
        oAuth2SignupProvider.provide(
          Mockito.any(OidcUser.class),
          Mockito.any(AuthProvider.class)
        )
      )
      .thenReturn(user);
  }

  @Test
  void testSuccessWithUserFoundInDB() {
    customOidcUserService.processOidcUser(oidcUserRequest, oidcUser);
    Mockito
      .verify(oAuth2PreVerifyProvider, Mockito.times(1))
      .provide(Mockito.any(User.class), Mockito.any(OidcUser.class));
  }

  @SuppressWarnings("unchecked")
  @Test
  void testSuccessWithUserNotFoundInDB() {
    Mockito
      .when(userRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.ofNullable(null));
    customOidcUserService.processOidcUser(oidcUserRequest, oidcUser);
    Mockito
      .verify(oAuth2SignupProvider, Mockito.times(1))
      .provide(Mockito.any(OidcUser.class), Mockito.any(AuthProvider.class));
  }

  @SuppressWarnings("unchecked")
  @Test
  void testProcessOidcUserFailedDueToUserCredentialsNotFound() {
    Mockito
      .when(userCredentialRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.ofNullable(null));
    assertThrows(
      OAuth2AuthenticationException.class,
      () -> customOidcUserService.processOidcUser(oidcUserRequest, oidcUser)
    );
  }

  @SuppressWarnings("unchecked")
  @Test
  void testProcessOidcUserFailedDueToUserCredentialsAuthIdNotMatches() {
    UserCredential userCredential = MockObjects.createMockUserCredentials(
      MockObjects.USER_EMAIL,
      MockObjects.CLIENT_ID
    );
    Mockito
      .when(userCredentialRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.of(userCredential));
    assertThrows(
      OAuth2AuthenticationException.class,
      () -> customOidcUserService.processOidcUser(oidcUserRequest, oidcUser)
    );
  }

  @SuppressWarnings("unchecked")
  @Test
  void testProcessOidcUserFailedDueToUserCredentialsAuthProviderNotMatches() {
    UserCredential userCredential = MockObjects.createMockUserCredentials(
      MockObjects.USER_USERNAME,
      AuthProvider.GITHUB.toString()
    );
    Mockito
      .when(userCredentialRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.of(userCredential));

    assertThrows(
      OAuth2AuthenticationException.class,
      () -> customOidcUserService.processOidcUser(oidcUserRequest, oidcUser)
    );
  }

  @SuppressWarnings("unchecked")
  @Test
  void testProcessOidcUserFailedDueToUserTenantNotFound() {
    Mockito
      .when(userTenantRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.ofNullable(null));
    assertThrows(
      OAuth2AuthenticationException.class,
      () -> customOidcUserService.processOidcUser(oidcUserRequest, oidcUser)
    );
  }

  @Test
  void testProcessOidcUserFailedDueToRoleNotFound() {
    Mockito
      .when(roleRepository.findById(Mockito.any(UUID.class)))
      .thenReturn(Optional.ofNullable(null));
    assertThrows(
      OAuth2AuthenticationException.class,
      () -> customOidcUserService.processOidcUser(oidcUserRequest, oidcUser)
    );
  }

  @Test
  void testLoaduserFailsDueToSuperClassMethodInvocationFailed() {
    assertThrows(
      InternalAuthenticationServiceException.class,
      () -> customOidcUserService.loadUser(oidcUserRequest)
    );
  }
}
