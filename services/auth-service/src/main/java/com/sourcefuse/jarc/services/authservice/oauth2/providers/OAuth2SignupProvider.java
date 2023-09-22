package com.sourcefuse.jarc.services.authservice.oauth2.providers;

import com.sourcefuse.jarc.core.enums.RoleKey;
import com.sourcefuse.jarc.services.authservice.dtos.RegisterDto;
import com.sourcefuse.jarc.services.authservice.enums.AuthErrorKeys;
import com.sourcefuse.jarc.services.authservice.enums.AuthProvider;
import com.sourcefuse.jarc.services.authservice.models.Role;
import com.sourcefuse.jarc.services.authservice.models.Tenant;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.oauth2.user.OAuth2UserInfo;
import com.sourcefuse.jarc.services.authservice.repositories.RoleRepository;
import com.sourcefuse.jarc.services.authservice.repositories.TenantRepository;
import com.sourcefuse.jarc.services.authservice.services.UserService;
import com.sourcefuse.jarc.services.authservice.specifications.RoleSpecification;
import com.sourcefuse.jarc.services.authservice.specifications.TenantSpecification;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@Service
@RequiredArgsConstructor
public class OAuth2SignupProvider {

  private final TenantRepository tenantRepository;
  private final UserService userService;
  private final RoleRepository roleRepository;

  public User provide(OAuth2UserInfo oAuth2UserInfo, AuthProvider provider) {
    Optional<Tenant> tenant =
      this.tenantRepository.findOne(TenantSpecification.byKey("master"));
    if (tenant.isEmpty()) {
      throw new HttpServerErrorException(
        HttpStatus.UNAUTHORIZED,
        AuthErrorKeys.INVALID_CREDENTIALS.toString()
      );
    }
    Optional<Role> defaultRole =
      this.roleRepository.findOne(
          RoleSpecification.byRoleType(RoleKey.DEFAULT)
        );
    if (defaultRole.isEmpty()) {
      throw new HttpServerErrorException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Role not found"
      );
    }
    String fullname = oAuth2UserInfo.getName().replace("( )+", " ");
    List<String> name = Arrays.asList(fullname.split(" "));
    String firstName = name.size() > 0 ? name.get(0) : null;
    String lastName = name.size() > 1 ? name.get(name.size() - 1) : null;

    User userToCreate = new User();
    userToCreate.setUsername(oAuth2UserInfo.getEmail());
    userToCreate.setFirstName(firstName);
    userToCreate.setLastName(lastName);
    userToCreate.setEmail(oAuth2UserInfo.getEmail());

    RegisterDto registerDto = new RegisterDto();
    registerDto.setAuthProvider(provider);
    registerDto.setDefaultTenantId(tenant.get().getId());
    registerDto.setUser(userToCreate);
    registerDto.setAuthId(oAuth2UserInfo.getId());
    registerDto.setRoleId(defaultRole.get().getId());

    return this.userService.register(registerDto);
  }

  public User provide(OidcUser oidcUser, AuthProvider provider) {
    Optional<Tenant> tenant =
      this.tenantRepository.findOne(TenantSpecification.byKey("master"));
    if (tenant.isEmpty()) {
      throw new HttpServerErrorException(
        HttpStatus.UNAUTHORIZED,
        AuthErrorKeys.INVALID_CREDENTIALS.toString()
      );
    }
    Optional<Role> defaultRole =
      this.roleRepository.findOne(
          RoleSpecification.byRoleType(RoleKey.DEFAULT)
        );
    if (defaultRole.isEmpty()) {
      throw new HttpServerErrorException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Role not found"
      );
    }
    User userToCreate = new User();
    userToCreate.setUsername(oidcUser.getPreferredUsername());
    userToCreate.setFirstName(oidcUser.getGivenName());
    userToCreate.setLastName(oidcUser.getFamilyName());
    userToCreate.setEmail(oidcUser.getEmail());

    RegisterDto registerDto = new RegisterDto();
    registerDto.setAuthProvider(provider);
    registerDto.setDefaultTenantId(tenant.get().getId());
    registerDto.setUser(userToCreate);
    registerDto.setAuthId(oidcUser.getPreferredUsername());
    registerDto.setRoleId(defaultRole.get().getId());

    return this.userService.register(registerDto);
  }

  public void verifyOAuth2UserEmail(String email) {
    if (email == null || email.isBlank()) {
      throw new OAuth2AuthenticationException(
        "Email not found from OAuth2 provider"
      );
    }
  }
}
