package com.sourcefuse.jarc.services.authservice.services;

import com.sourcefuse.jarc.services.authservice.enums.AuthErrorKeys;
import com.sourcefuse.jarc.services.authservice.enums.AuthProvider;
import com.sourcefuse.jarc.services.authservice.enums.RoleKey;
import com.sourcefuse.jarc.services.authservice.enums.UserStatus;
import com.sourcefuse.jarc.services.authservice.exception.CommonRuntimeException;
import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.models.Role;
import com.sourcefuse.jarc.services.authservice.models.Tenant;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserCredential;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import com.sourcefuse.jarc.services.authservice.payload.RegisterDto;
import com.sourcefuse.jarc.services.authservice.repositories.AuthClientRepository;
import com.sourcefuse.jarc.services.authservice.repositories.RoleRepository;
import com.sourcefuse.jarc.services.authservice.repositories.TenantRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserCredentialRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserTenantRepository;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

  @Value("${app.config.default-password}")
  String defaultPassword;

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final TenantRepository tenantRepository;
  private final AuthClientRepository authClientRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserCredentialRepository userCredentialRepository;
  private final UserTenantRepository userTenantRepository;

  public User register(RegisterDto registerDto) {
    checkIfUserExists(registerDto);
    Optional<Tenant> tenant = tenantRepository.findByKey("master");
    if (tenant.isEmpty()) {
      throw new HttpServerErrorException(
        HttpStatus.UNAUTHORIZED,
        AuthErrorKeys.INVALID_CREDENTIALS.label
      );
    }
    Optional<Role> defaultRole = roleRepository.findByRoleType(
      RoleKey.DEFAULT.label
    );
    if (defaultRole.isEmpty()) {
      throw new HttpServerErrorException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Role not found"
      );
    }

    Optional<User> user =
      this.userRepository.findFirstUserByUsernameOrEmail(
          registerDto.getUser().getUsername().toLowerCase(Locale.getDefault())
        );
    if (user.isPresent()) {
      Optional<UserTenant> userTenant =
        this.userTenantRepository.findUserBy(
            user.get().getId(),
            tenant.get().getId()
          );
      if (userTenant.isPresent()) {
        throw new CommonRuntimeException(
          HttpStatus.BAD_REQUEST,
          "User already exists and belongs to this tenant"
        );
      } else {
        this.createUserTenantData(
            UserStatus.ACTIVE,
            user.get().getId(),
            defaultRole.get().getId(),
            tenant.get().getId()
          );
        return user.get();
      }
    }

    return createUser(registerDto, tenant.get(), defaultRole.get());
  }

  User createUser(RegisterDto registerDto, Tenant tenant, Role defaultRole) {
    ArrayList<AuthClient> authClients =
      this.authClientRepository.findByAllowedClients(
          defaultRole.getAllowedClients()
        );
    registerDto
      .getUser()
      .setAuthClientIds(
        authClients
          .stream()
          .map(AuthClient::getId)
          .collect(java.util.stream.Collectors.toList())
      );

    registerDto.getUser().setDefaultTenantId(tenant.getId());
    User savedUser =
      this.createUser(
          registerDto.getUser(),
          registerDto.getAuthProvider(),
          registerDto.getAuthId()
        );
    this.createUserTenantData(
        UserStatus.ACTIVE,
        savedUser.getId(),
        defaultRole.getId(),
        tenant.getId()
      );
    return savedUser;
  }

  void checkIfUserExists(RegisterDto registerDto) {
    if (
      Boolean.TRUE.equals(
        userRepository.existsByUsername(registerDto.getUser().getUsername())
      )
    ) {
      throw new CommonRuntimeException(
        HttpStatus.BAD_REQUEST,
        "Username is already exists!."
      );
    }
    Optional<User> userExists = userRepository.findByEmail(
      registerDto.getAuthId()
    );
    if (userExists.isPresent()) {
      throw new CommonRuntimeException(
        HttpStatus.BAD_REQUEST,
        "Email is already exists!."
      );
    }

    Optional<UserCredential> userCreds =
      this.userCredentialRepository.findByAuthIdAndAuthProvider(
          registerDto.getAuthId(),
          registerDto.getAuthProvider().label
        );
    if (userCreds.isPresent()) {
      throw new CommonRuntimeException(
        HttpStatus.BAD_REQUEST,
        "User already exists!."
      );
    }
  }

  User createUser(User user, AuthProvider provider, String authId) {
    try {
      Optional<User> userExists =
        this.userRepository.findUserByUsername(user.getUsername());
      if (userExists.isPresent()) {
        throw new CommonRuntimeException(
          HttpStatus.BAD_REQUEST,
          "User already exists!."
        );
      }

      user = this.userRepository.save(user);
      UserCredential userCredential = new UserCredential();
      userCredential.setUserId(user.getId());
      if (provider == AuthProvider.KEYCLOAK) {
        userCredential.setAuthId(authId);
        userCredential.setAuthProvider(AuthProvider.KEYCLOAK.label);
      } else {
        userCredential.setAuthProvider(AuthProvider.INTERNAL.label);
        userCredential.setPassword(passwordEncoder.encode(defaultPassword));
      }
      this.userCredentialRepository.save(userCredential);
    } catch (Exception e) {
      log.error("Error while creating user", e);
      throw new CommonRuntimeException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Error while creating user"
      );
    }
    return user;
  }

  UserTenant createUserTenantData(
    UserStatus status,
    UUID userId,
    UUID roleId,
    UUID tenantId
  ) {
    UserTenant userTenant = new UserTenant();
    userTenant.setRoleId(roleId);
    userTenant.setStatus(status);
    userTenant.setTenantId(tenantId);
    userTenant.setUserId(userId);
    userTenantRepository.save(userTenant);
    return userTenant;
  }
}
