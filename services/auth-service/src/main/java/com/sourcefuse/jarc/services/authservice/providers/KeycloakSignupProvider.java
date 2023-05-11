package com.sourcefuse.jarc.services.authservice.providers;

import com.sourcefuse.jarc.services.authservice.enums.AuthErrorKeys;
import com.sourcefuse.jarc.services.authservice.enums.AuthProvider;
import com.sourcefuse.jarc.services.authservice.enums.RoleKey;
import com.sourcefuse.jarc.services.authservice.models.Role;
import com.sourcefuse.jarc.services.authservice.models.Tenant;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.payload.RegisterDto;
import com.sourcefuse.jarc.services.authservice.payload.keycloak.KeycloakUserDTO;
import com.sourcefuse.jarc.services.authservice.repositories.RoleRepository;
import com.sourcefuse.jarc.services.authservice.repositories.TenantRepository;
import com.sourcefuse.jarc.services.authservice.services.UserService;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

@AllArgsConstructor
@Component
public class KeycloakSignupProvider {

  private final TenantRepository tenantRepository;
  private final UserService userService;
  private final RoleRepository roleRepository;

  public Optional<User> provide(KeycloakUserDTO keycloakUserDTO) {
    Optional<Tenant> tenant = this.tenantRepository.findByKey("master");
    Optional<Role> defaultRole =
      this.roleRepository.findByRoleType(RoleKey.DEFAULT.label);
    if (tenant.isEmpty()) {
      throw new HttpServerErrorException(
        HttpStatus.UNAUTHORIZED,
        AuthErrorKeys.INVALID_CREDENTIALS.label
      );
    }

    if (defaultRole.isEmpty()) {
      throw new HttpServerErrorException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Role not found"
      );
    }
    User userToCreate = new User();
    userToCreate.setUsername(keycloakUserDTO.getPreferred_username());
    userToCreate.setFirstName(keycloakUserDTO.getGiven_name());
    userToCreate.setLastName(keycloakUserDTO.getFamily_name());
    userToCreate.setEmail(keycloakUserDTO.getEmail());

    RegisterDto registerDto = new RegisterDto();
    registerDto.setAuthProvider(AuthProvider.KEYCLOAK);
    registerDto.setDefaultTenantId(tenant.get().getId());
    registerDto.setUser(userToCreate);
    registerDto.setAuthId(keycloakUserDTO.getPreferred_username());
    registerDto.setRoleId(defaultRole.get().getId());

    return Optional.ofNullable(this.userService.register(registerDto));
  }
}
