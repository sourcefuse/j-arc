package com.sourcefuse.jarc.services.authservice.services;

import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpServerErrorException;

import com.sourcefuse.jarc.core.enums.UserStatus;
import com.sourcefuse.jarc.core.exception.CommonRuntimeException;
import com.sourcefuse.jarc.services.authservice.dtos.CodeResponse;
import com.sourcefuse.jarc.services.authservice.dtos.JWTAuthResponse;
import com.sourcefuse.jarc.services.authservice.dtos.LoginDto;
import com.sourcefuse.jarc.services.authservice.enums.AuthErrorKeys;
import com.sourcefuse.jarc.services.authservice.enums.AuthProvider;
import com.sourcefuse.jarc.services.authservice.enums.AuthenticateErrorKeys;
import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.models.Role;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserCredential;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import com.sourcefuse.jarc.services.authservice.providers.AuthCodeGeneratorProvider;
import com.sourcefuse.jarc.services.authservice.providers.JwtTokenProvider;
import com.sourcefuse.jarc.services.authservice.repositories.RoleRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserCredentialRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserTenantRepository;
import com.sourcefuse.jarc.services.authservice.specifications.UserCredentialSpecification;
import com.sourcefuse.jarc.services.authservice.specifications.UserSpecification;
import com.sourcefuse.jarc.services.authservice.specifications.UserTenantSpecification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpServerErrorException;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final UserCredentialRepository userCredentialRepository;
  private final UserTenantRepository userTenantRepository;
  private final AuthCodeGeneratorProvider authCodeGeneratorProvider;
  private final JwtTokenProvider jwtTokenProvider;

  public CodeResponse login(
      LoginDto loginDto,
      AuthClient authClient,
      User user) {
    UserTenant userTenant = this.verifyClientUserLogin(loginDto, authClient, user);
    Role role = roleRepository
        .findById(userTenant.getRoleId())
        .orElseThrow(() -> new CommonRuntimeException(
            HttpStatus.UNAUTHORIZED,
            AuthenticateErrorKeys.UNPROCESSABLE_DATA.toString()));
    return this.authCodeGeneratorProvider.provide(
        user,
        userTenant,
        role,
        authClient);
  }

  public JWTAuthResponse loginToken(
      LoginDto loginDto,
      AuthClient authClient,
      User authUser) {
    UserTenant userTenant = this.verifyClientUserLogin(loginDto, authClient, authUser);
    Role role = roleRepository
        .findById(userTenant.getRoleId())
        .orElseThrow(() -> new CommonRuntimeException(
            HttpStatus.UNAUTHORIZED,
            AuthenticateErrorKeys.UNPROCESSABLE_DATA.toString()));
    return this.jwtTokenProvider.createJwt(
        authUser,
        userTenant,
        role,
        authClient);
  }

  public Optional<User> verifyPassword(String username, String password) {
    Optional<User> user = userRepository.findOne(
        UserSpecification.byUsername(username.toLowerCase()));
    if (user.isEmpty() || user.get().isDeleted()) {
      throw new HttpServerErrorException(
          HttpStatus.UNAUTHORIZED,
          AuthenticateErrorKeys.USER_DOES_NOT_EXISTS.toString());
    }
    Optional<UserCredential> userCredential = this.userCredentialRepository.findOne(
        UserCredentialSpecification.byUserId(user.get().getId()));

    if (userCredential.isPresent() &&
        (userCredential.get().getPassword().isEmpty() ||
            !Objects.equals(
                userCredential.get().getAuthProvider(),
                AuthProvider.INTERNAL.toString())
            ||
            !(BCrypt.checkpw(password, userCredential.get().getPassword())))) {
      log.error("User credentials not found in DB or is invalid");
      throw new HttpServerErrorException(
          HttpStatus.UNAUTHORIZED,
          AuthErrorKeys.INVALID_CREDENTIALS.toString());
    } else {
      return user;
    }
  }

  public UserTenant verifyClientUserLogin(
      LoginDto req,
      AuthClient client,
      User user) {
    User currentUser = user;

    Optional<UserTenant> userTenant = userTenantRepository.findOne(
        UserTenantSpecification.byUserId(currentUser.getId()));
    if (userTenant.isEmpty()) {
      throw new HttpServerErrorException(
          HttpStatus.UNAUTHORIZED,
          AuthenticateErrorKeys.USER_DOES_NOT_EXISTS.toString());
    }
    UserStatus userStatus = userTenant.get().getStatus();

    if (currentUser.getAuthClientIds().isEmpty()) {
      log.error("No allowed auth clients found for this user in DB");
      throw new HttpServerErrorException(
          HttpStatus.UNPROCESSABLE_ENTITY,
          AuthErrorKeys.CLIENT_USER_MISSING.toString());
    } else if (!StringUtils.hasLength(req.getClientSecret())) {
      log.error("client secret key missing from request object");
      throw new HttpServerErrorException(
          HttpStatus.BAD_REQUEST,
          AuthErrorKeys.CLIENT_SECRET_MISSING.toString());
    } else if (!currentUser.getAuthClientIds().contains(client.getId())) {
      log.error("User is not allowed to access client id passed in request");
      throw new HttpServerErrorException(
          HttpStatus.UNAUTHORIZED,
          AuthErrorKeys.CLIENT_INVALID.toString());
    } else if (userStatus == UserStatus.REGISTERED) {
      log.error("User is in registered state");
      throw new HttpServerErrorException(
          HttpStatus.BAD_REQUEST,
          "User not active yet");
    } else {
      // for sonar
    }
    return userTenant.get();
  }
}
