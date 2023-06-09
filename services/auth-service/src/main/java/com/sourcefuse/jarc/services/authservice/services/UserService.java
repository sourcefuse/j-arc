package com.sourcefuse.jarc.services.authservice.services;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import com.sourcefuse.jarc.core.enums.RoleKey;
import com.sourcefuse.jarc.core.enums.UserStatus;
import com.sourcefuse.jarc.core.exception.CommonRuntimeException;
import com.sourcefuse.jarc.services.authservice.dtos.RegisterDto;
import com.sourcefuse.jarc.services.authservice.enums.AuthErrorKeys;
import com.sourcefuse.jarc.services.authservice.enums.AuthProvider;
import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.models.Role;
import com.sourcefuse.jarc.services.authservice.models.Tenant;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserCredential;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import com.sourcefuse.jarc.services.authservice.repositories.AuthClientRepository;
import com.sourcefuse.jarc.services.authservice.repositories.RoleRepository;
import com.sourcefuse.jarc.services.authservice.repositories.TenantRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserCredentialRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserTenantRepository;
import com.sourcefuse.jarc.services.authservice.specifications.AuthClientSpecification;
import com.sourcefuse.jarc.services.authservice.specifications.RoleSpecification;
import com.sourcefuse.jarc.services.authservice.specifications.TenantSpecification;
import com.sourcefuse.jarc.services.authservice.specifications.UserCredentialSpecification;
import com.sourcefuse.jarc.services.authservice.specifications.UserSpecification;
import com.sourcefuse.jarc.services.authservice.specifications.UserTenantSpecification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    Optional<Tenant> tenant =
      this.tenantRepository.findOne(TenantSpecification.byKey("master"));
    if (tenant.isEmpty()) {
      throw new HttpServerErrorException(
        HttpStatus.UNAUTHORIZED,
        AuthErrorKeys.INVALID_CREDENTIALS.toString()
      );
    }
    Optional<Role> defaultRole = roleRepository.findOne(
      RoleSpecification.byRoleType(RoleKey.DEFAULT)
    );
    if (defaultRole.isEmpty()) {
      throw new HttpServerErrorException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Role not found"
      );
    }

    Optional<User> user =
      this.userRepository.findOne(
          UserSpecification.byUsername(registerDto.getUser().getUsername())
        );
    if (user.isPresent()) {
      Optional<UserTenant> userTenant =
        this.userTenantRepository.findOne(
            UserTenantSpecification.byUserIdAndTenantId(
              user.get().getId(),
              tenant.get().getId()
            )
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
        (ArrayList<AuthClient>) this.authClientRepository.findAll(
        AuthClientSpecification.byAllowedClients(
          defaultRole.getAllowedClients()
        )
      );
    registerDto
      .getUser()
      .setAuthClientIds(
        authClients.stream().map(AuthClient::getId).toList()
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
        userRepository.exists(
          UserSpecification.byUsername(registerDto.getUser().getUsername())
        )
      )
    ) {
      throw new CommonRuntimeException(
        HttpStatus.BAD_REQUEST,
        "Username is already exists!."
      );
    }
    Optional<User> userExists = userRepository.findOne(
      UserSpecification.byEmail(registerDto.getAuthId())
    );
    if (userExists.isPresent()) {
      throw new CommonRuntimeException(
        HttpStatus.BAD_REQUEST,
        "Email is already exists!."
      );
    }

    Optional<UserCredential> userCreds =
      this.userCredentialRepository.findOne(
          UserCredentialSpecification.byAuthIdAndAuthProvider(
            registerDto.getAuthId(),
            registerDto.getAuthProvider().toString()
          )
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
      Optional<User> userExists = userRepository.findOne(
        UserSpecification.byUsername(user.getUsername())
      );
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
        userCredential.setAuthProvider(AuthProvider.KEYCLOAK.toString());
      } else {
        userCredential.setAuthProvider(AuthProvider.INTERNAL.toString());
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
