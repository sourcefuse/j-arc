package com.sourcefuse.jarc.services.authservice.tests.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sourcefuse.jarc.services.authservice.dtos.RegisterDto;
import com.sourcefuse.jarc.services.authservice.enums.AuthProvider;
import com.sourcefuse.jarc.services.authservice.models.Role;
import com.sourcefuse.jarc.services.authservice.models.Tenant;
import com.sourcefuse.jarc.services.authservice.oauth2.providers.OAuth2SignupProvider;
import com.sourcefuse.jarc.services.authservice.oauth2.user.OAuth2UserInfo;
import com.sourcefuse.jarc.services.authservice.repositories.RoleRepository;
import com.sourcefuse.jarc.services.authservice.repositories.TenantRepository;
import com.sourcefuse.jarc.services.authservice.services.UserService;
import com.sourcefuse.jarc.services.authservice.tests.mocks.MockObjects;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.client.HttpServerErrorException;

class OAuth2SignupProviderTests {

  @Mock
  TenantRepository tenantRepository;

  @Mock
  UserService userService;

  @Mock
  RoleRepository roleRepository;

  @InjectMocks
  OAuth2SignupProvider oAuth2SignupProvider;

  OidcUser oidcUser;
  OAuth2UserInfo oAuth2User;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    oidcUser = MockObjects.createMockOidcUser();
    oAuth2User = MockObjects.createMockOAuth2UserInfo();
  }

  @SuppressWarnings("unchecked")
  @Test
  void testOidcProvideThrowExceptionWhenTenantIsNull() {
    Mockito
      .when(tenantRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.ofNullable(null));
    assertThrows(
      HttpServerErrorException.class,
      () -> oAuth2SignupProvider.provide(oidcUser, AuthProvider.GOOGLE)
    );
  }

  @SuppressWarnings("unchecked")
  @Test
  void testOidcProvideThrowExceptionWhenRoleIsNull() {
    Mockito
      .when(tenantRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.of(Mockito.mock(Tenant.class)));
    Mockito
      .when(roleRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.ofNullable(null));
    assertThrows(
      HttpServerErrorException.class,
      () -> oAuth2SignupProvider.provide(oidcUser, AuthProvider.GOOGLE)
    );
  }

  @SuppressWarnings("unchecked")
  @Test
  void testOidcProvideSuccess() {
    Mockito
      .when(tenantRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.of(Mockito.mock(Tenant.class)));
    Mockito
      .when(roleRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.of(Mockito.mock(Role.class)));
    oAuth2SignupProvider.provide(oidcUser, AuthProvider.GOOGLE);

    Mockito
      .verify(userService, Mockito.times(1))
      .register(Mockito.any(RegisterDto.class));
  }

  @SuppressWarnings("unchecked")
  @Test
  void testOAuth2UserProvideThrowExceptionWhenTenantIsNull() {
    Mockito
      .when(tenantRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.ofNullable(null));
    assertThrows(
      HttpServerErrorException.class,
      () -> oAuth2SignupProvider.provide(oAuth2User, AuthProvider.GOOGLE)
    );
  }

  @SuppressWarnings("unchecked")
  @Test
  void testOAuth2UserProvideThrowExceptionWhenRoleIsNull() {
    Mockito
      .when(tenantRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.of(Mockito.mock(Tenant.class)));
    Mockito
      .when(roleRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.ofNullable(null));
    assertThrows(
      HttpServerErrorException.class,
      () -> oAuth2SignupProvider.provide(oAuth2User, AuthProvider.GOOGLE)
    );
  }

  @SuppressWarnings("unchecked")
  @Test
  void testOAuth2UserProvideSuccess() {
    Mockito
      .when(tenantRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.of(Mockito.mock(Tenant.class)));
    Mockito
      .when(roleRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.of(Mockito.mock(Role.class)));
    oAuth2SignupProvider.provide(oAuth2User, AuthProvider.GOOGLE);

    Mockito
      .verify(userService, Mockito.times(1))
      .register(Mockito.any(RegisterDto.class));
  }

  @Test
  void testVerifyOAuth2UserEmailThrowErrorWhenEmailIsNull() {
    assertThrows(
      OAuth2AuthenticationException.class,
      () -> oAuth2SignupProvider.verifyOAuth2UserEmail(null)
    );
  }

  @Test
  void testVerifyOAuth2UserEmailThrowErrorWhenEmailIsEmpty() {
    assertThrows(
      OAuth2AuthenticationException.class,
      () -> oAuth2SignupProvider.verifyOAuth2UserEmail("")
    );
  }

  @Test
  void testVerifyOAuth2UserEmailThrowErrorWhenEmailHasOnlySpaces() {
    assertThrows(
      OAuth2AuthenticationException.class,
      () -> oAuth2SignupProvider.verifyOAuth2UserEmail("  ")
    );
  }
}
