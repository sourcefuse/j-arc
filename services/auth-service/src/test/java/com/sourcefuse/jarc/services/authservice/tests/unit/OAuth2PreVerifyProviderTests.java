package com.sourcefuse.jarc.services.authservice.tests.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sourcefuse.jarc.core.enums.UserStatus;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import com.sourcefuse.jarc.services.authservice.oauth2.providers.OAuth2PreVerifyProvider;
import com.sourcefuse.jarc.services.authservice.oauth2.user.OAuth2UserInfo;
import com.sourcefuse.jarc.services.authservice.repositories.UserRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserTenantRepository;
import com.sourcefuse.jarc.services.authservice.tests.mocks.MockObjects;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.client.HttpServerErrorException;

class OAuth2PreVerifyProviderTests {

  @Mock
  UserRepository userRepository;

  @Mock
  UserTenantRepository userTenantRepository;

  @InjectMocks
  OAuth2PreVerifyProvider oAuth2PreVerifyProvider;

  User user;
  OidcUser oidcUser;
  OAuth2UserInfo oAuth2User;

  @SuppressWarnings("unchecked")
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    user = MockObjects.createMockUser();
    oidcUser = MockObjects.createMockOidcUser();
    oAuth2User = MockObjects.createMockOAuth2UserInfo();

    Mockito
      .when(userTenantRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.of(Mockito.mock(UserTenant.class)));
  }

  @Test
  void testOidcProvideShouldUpdateUserSinceFirstNameIsDiff() {
    Mockito.when(oidcUser.getGivenName()).thenReturn("ABCD");
    oAuth2PreVerifyProvider.provide(user, oidcUser);
    Mockito
      .verify(userRepository, Mockito.times(1))
      .save(Mockito.any(User.class));
  }

  @Test
  void testOidcProvideShouldUpdateUserSinceLastNameIsDiff() {
    Mockito.when(oidcUser.getFamilyName()).thenReturn("XYZA");
    oAuth2PreVerifyProvider.provide(user, oidcUser);
    Mockito
      .verify(userRepository, Mockito.times(1))
      .save(Mockito.any(User.class));
  }

  @Test
  void testOidcProvideShouldUpdateUserSinceEmailNameIsDiff() {
    Mockito.when(oidcUser.getEmail()).thenReturn("user1@email.com");
    oAuth2PreVerifyProvider.provide(user, oidcUser);
    Mockito
      .verify(userRepository, Mockito.times(1))
      .save(Mockito.any(User.class));
  }

  @Test
  void testOidcProvideShouldUpdateUserSinceUserNameIsDiff() {
    Mockito.when(oidcUser.getPreferredUsername()).thenReturn("username");
    oAuth2PreVerifyProvider.provide(user, oidcUser);
    Mockito
      .verify(userRepository, Mockito.times(1))
      .save(Mockito.any(User.class));
  }

  @Test
  void testOidcProvideShouldNotUpdateUser() {
    oAuth2PreVerifyProvider.provide(user, oidcUser);
    Mockito
      .verify(userRepository, Mockito.times(0))
      .save(Mockito.any(User.class));
  }

  @SuppressWarnings("unchecked")
  @Test
  void testOidcProvideShouldThrowErrorWhenUserTeantEmpty() {
    Mockito
      .when(userTenantRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.ofNullable(null));
    assertThrows(
      HttpServerErrorException.class,
      () -> oAuth2PreVerifyProvider.provide(user, oidcUser)
    );
  }

  @SuppressWarnings("unchecked")
  @Test
  void testOidcUserProvideShouldActivateUserTenantIfStatusIsRegistered() {
    UserTenant userTenant = Mockito.mock(UserTenant.class);
    Mockito.when(userTenant.getStatus()).thenReturn(UserStatus.REGISTERED);
    Mockito
      .when(userTenantRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.of(userTenant));
    oAuth2PreVerifyProvider.provide(user, oidcUser);
    Mockito
      .verify(userTenantRepository, Mockito.times(1))
      .save(Mockito.any(UserTenant.class));
  }

  @Test
  void testOAuthUserProvideShouldUpdateUserSinceUsernameIsDiff() {
    oAuth2PreVerifyProvider.provide(user, oAuth2User);
    Mockito
      .verify(userRepository, Mockito.times(1))
      .save(Mockito.any(User.class));
  }

  @Test
  void testOAuthUserProvideShouldUpdateUserSinceEmailIsDiff() {
    user.setUsername(oAuth2User.getEmail());
    user.setEmail("usr@email.com");
    oAuth2PreVerifyProvider.provide(user, oAuth2User);
    Mockito
      .verify(userRepository, Mockito.times(1))
      .save(Mockito.any(User.class));
  }

  @Test
  void testOAuthUserProvideShouldUpdateUserSinceFirstIsDiff() {
    user.setUsername(oAuth2User.getEmail());
    user.setFirstName("firstName");
    oAuth2PreVerifyProvider.provide(user, oAuth2User);
    Mockito
      .verify(userRepository, Mockito.times(1))
      .save(Mockito.any(User.class));
  }

  @Test
  void testOAuthUserProvideShouldUpdateUserSinceLastNameIsDiff() {
    Mockito.when(user.getUsername()).thenReturn(MockObjects.USER_EMAIL);
    Mockito.when(user.getLastName()).thenReturn("lastname");
    oAuth2PreVerifyProvider.provide(user, oAuth2User);
    Mockito
      .verify(userRepository, Mockito.times(1))
      .save(Mockito.any(User.class));
  }

  @Test
  void testOAuthUserProvideShouldNotUpdateUserSinceNameIsEmptyInUserInfo() {
    Mockito.when(user.getUsername()).thenReturn(MockObjects.USER_EMAIL);
    Mockito.when(oAuth2User.getName()).thenReturn("");
    oAuth2PreVerifyProvider.provide(user, oAuth2User);
    Mockito
      .verify(userRepository, Mockito.times(0))
      .save(Mockito.any(User.class));
  }

  @Test
  void testOAuthUserProvideShouldNotUpdateUserSinceNameHasSpaceInUserInfo() {
    Mockito.when(user.getUsername()).thenReturn(MockObjects.USER_EMAIL);
    Mockito.when(oAuth2User.getName()).thenReturn("       ");
    oAuth2PreVerifyProvider.provide(user, oAuth2User);
    Mockito
      .verify(userRepository, Mockito.times(0))
      .save(Mockito.any(User.class));
  }

  @Test
  void testOAuthUserProvideShouldNotUpdateUser() {
    Mockito.when(user.getUsername()).thenReturn(MockObjects.USER_EMAIL);
    oAuth2PreVerifyProvider.provide(user, oAuth2User);
    Mockito
      .verify(userRepository, Mockito.times(0))
      .save(Mockito.any(User.class));
  }

  @SuppressWarnings("unchecked")
  @Test
  void testOAuthUserProvideShouldThrowErrorWhenUserTeantEmpty() {
    Mockito
      .when(userTenantRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.ofNullable(null));
    assertThrows(
      HttpServerErrorException.class,
      () -> oAuth2PreVerifyProvider.provide(user, oAuth2User)
    );
  }

  @SuppressWarnings("unchecked")
  @Test
  void testOAuthUserProvideShouldActivateUserTenantIfStatusIsRegistered() {
    UserTenant userTenant = Mockito.mock(UserTenant.class);
    Mockito.when(userTenant.getStatus()).thenReturn(UserStatus.REGISTERED);
    Mockito
      .when(userTenantRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.of(userTenant));
    oAuth2PreVerifyProvider.provide(user, oAuth2User);
    Mockito
      .verify(userTenantRepository, Mockito.times(1))
      .save(Mockito.any(UserTenant.class));
  }
}
