package com.sourcefuse.jarc.services.authservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcefuse.jarc.services.authservice.enums.AuthErrorKeys;
import com.sourcefuse.jarc.services.authservice.enums.AuthProvider;
import com.sourcefuse.jarc.services.authservice.enums.AuthenticateErrorKeys;
import com.sourcefuse.jarc.services.authservice.enums.RoleKey;
import com.sourcefuse.jarc.services.authservice.enums.UserStatus;
import com.sourcefuse.jarc.services.authservice.exception.CommonRuntimeException;
import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.models.JwtTokenRedis;
import com.sourcefuse.jarc.services.authservice.models.RefreshTokenRedis;
import com.sourcefuse.jarc.services.authservice.models.RevokedTokenRedis;
import com.sourcefuse.jarc.services.authservice.models.Role;
import com.sourcefuse.jarc.services.authservice.models.Tenant;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserCredential;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import com.sourcefuse.jarc.services.authservice.payload.AuthTokenRequest;
import com.sourcefuse.jarc.services.authservice.payload.CodeResponse;
import com.sourcefuse.jarc.services.authservice.payload.JWTAuthResponse;
import com.sourcefuse.jarc.services.authservice.payload.LoginDto;
import com.sourcefuse.jarc.services.authservice.payload.RefreshTokenDTO;
import com.sourcefuse.jarc.services.authservice.payload.RegisterDto;
import com.sourcefuse.jarc.services.authservice.providers.AuthCodeGeneratorProvider;
import com.sourcefuse.jarc.services.authservice.providers.JwtTokenProvider;
import com.sourcefuse.jarc.services.authservice.repositories.AuthClientRepository;
import com.sourcefuse.jarc.services.authservice.repositories.RoleRepository;
import com.sourcefuse.jarc.services.authservice.repositories.TenantRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserCredentialRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserTenantRepository;
import com.sourcefuse.jarc.services.authservice.session.CurrentUser;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
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

  @Value("${app.config.default-password}")
  String defaultPassword;

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final TenantRepository tenantRepository;
  private final AuthClientRepository authClientRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserCredentialRepository userCredentialRepository;
  private final UserTenantRepository userTenantRepository;
  private final AuthCodeGeneratorProvider authCodeGeneratorProvider;
  private final JwtTokenProvider jwtTokenProvider;
  private final RedisTemplate<String, Object> redisTemplate;

  public JWTAuthResponse getTokenByCode(AuthTokenRequest authTokenRequest) {
    AuthClient authClient =
      this.authClientRepository.findAuthClientByClientId(
          authTokenRequest.getClientId()
        )
        .orElseThrow(() ->
          new CommonRuntimeException(
            HttpStatus.UNAUTHORIZED,
            AuthErrorKeys.CLIENT_INVALID.label
          )
        );
    JwtTokenRedis jwtTokenObject = (JwtTokenRedis) this.redisTemplate.opsForValue()
      .get(authTokenRequest.getCode());

    CurrentUser currentUser = jwtTokenProvider.getUserDetails(
      jwtTokenObject.getToken()
    );

    return this.jwtTokenProvider.createJwt(
        currentUser.getUser(),
        currentUser.getUserTenant(),
        currentUser.getRole(),
        authClient
      );
  }

  public CodeResponse login(
    LoginDto loginDto,
    AuthClient authClient,
    User user
  ) {
    UserTenant userTenant =
      this.verifyClientUserLogin(loginDto, authClient, user);
    Role role = roleRepository
      .findById(userTenant.getRoleId())
      .orElseThrow(() ->
        new CommonRuntimeException(
          HttpStatus.UNAUTHORIZED,
          AuthenticateErrorKeys.UNPROCESSABLE_DATA.label
        )
      );
    return this.authCodeGeneratorProvider.provide(
        user,
        userTenant,
        role,
        authClient
      );
  }

  public JWTAuthResponse loginToken(
    LoginDto loginDto,
    AuthClient authClient,
    User authUser
  ) {
    UserTenant userTenant =
      this.verifyClientUserLogin(loginDto, authClient, authUser);
    Role role = roleRepository
      .findById(userTenant.getRoleId())
      .orElseThrow(() ->
        new CommonRuntimeException(
          HttpStatus.UNAUTHORIZED,
          AuthenticateErrorKeys.UNPROCESSABLE_DATA.label
        )
      );
    return this.jwtTokenProvider.createJwt(
        authUser,
        userTenant,
        role,
        authClient
      );
  }

  public JWTAuthResponse refreshToken(
    String authorizationHeader,
    RefreshTokenDTO refreshTokenDTO
  ) {
    RefreshTokenRedis refreshTokenRedis = (RefreshTokenRedis) this.redisTemplate.opsForValue()
      .get(refreshTokenDTO.getRefreshToken().toString());

    RefreshTokenRedis refreshTokenRedis2 = new ObjectMapper()
      .convertValue(refreshTokenRedis, RefreshTokenRedis.class);
    AuthClient client = authClientRepository
      .findAuthClientByClientId(refreshTokenRedis2.getClientId())
      .orElseThrow(() ->
        new HttpServerErrorException(
          HttpStatus.UNAUTHORIZED,
          AuthErrorKeys.CLIENT_INVALID.label
        )
      );
    String accessToken = authorizationHeader.split(" ")[1];
    if (!accessToken.equals(refreshTokenRedis.getExternalAuthToken())) {
      throw new HttpServerErrorException(
        HttpStatus.UNAUTHORIZED,
        AuthErrorKeys.TOKEN_INVALID.label
      );
    }
    CurrentUser currentUser = (CurrentUser) SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getPrincipal();
    this.setWithTtl(
        accessToken,
        new RevokedTokenRedis(accessToken, accessToken),
        client.getRefreshTokenExpiration()
      );
    this.redisTemplate.delete(refreshTokenRedis.getId());
    User user = userRepository
      .findById(refreshTokenRedis.getUserId())
      .orElseThrow(() ->
        new HttpServerErrorException(
          HttpStatus.UNAUTHORIZED,
          AuthenticateErrorKeys.USER_DOES_NOT_EXISTS.label
        )
      );

    UserTenant userTenant = userTenantRepository
      .findById(currentUser.getUserTenant().getId())
      .orElseThrow(() ->
        new HttpServerErrorException(
          HttpStatus.UNAUTHORIZED,
          AuthenticateErrorKeys.USER_DOES_NOT_EXISTS.label
        )
      );

    Role role = roleRepository
      .findById(currentUser.getUserTenant().getRoleId())
      .orElseThrow(() ->
        new HttpServerErrorException(
          HttpStatus.UNAUTHORIZED,
          AuthenticateErrorKeys.USER_DOES_NOT_EXISTS.label
        )
      );

    return jwtTokenProvider.createJwt(user, userTenant, role, client);
  }

  public void setWithTtl(String key, Object value, long ttl) {
    ValueOperations<String, Object> ops = redisTemplate.opsForValue();
    ops.set(key, value, ttl, TimeUnit.SECONDS);
  }

  public User register(RegisterDto registerDto) {
    if (userRepository.existsByUsername(registerDto.getUser().getUsername())) {
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
    Optional<Role> defaultRole = roleRepository.findByRoleType(
      RoleKey.DEFAULT.label
    );
    Optional<Tenant> tenant = tenantRepository.findByKey("master");
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
    Optional<User> user =
      this.userRepository.findFirstUserByUsernameOrEmail(
          registerDto.getUser().getUsername().toLowerCase()
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
            registerDto.getUser(),
            UserStatus.ACTIVE,
            user.get().getId(),
            defaultRole.get().getId(),
            tenant.get().getId()
          );
        return user.get();
      }
    }
    ArrayList<AuthClient> authClients =
      this.authClientRepository.findByAllowedClients(
          defaultRole.get().getAllowedClients()
        );
    registerDto
      .getUser()
      .setAuthClientIds(
        authClients
          .stream()
          .map(AuthClient::getId)
          .collect(java.util.stream.Collectors.toList())
      );

    registerDto.getUser().setDefaultTenantId(tenant.get().getId());
    User savedUser =
      this.createUser(
          registerDto.getUser(),
          registerDto.getAuthProvider(),
          registerDto.getAuthId()
        );
    this.createUserTenantData(
        savedUser,
        UserStatus.ACTIVE,
        savedUser.getId(),
        defaultRole.get().getId(),
        tenant.get().getId()
      );
    return savedUser;
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
      switch (provider) {
        case KEYCLOAK:
          userCredential.setAuthId(authId);
          userCredential.setAuthProvider(AuthProvider.KEYCLOAK.label);
          break;
        default:
          userCredential.setAuthProvider(AuthProvider.INTERNAL.label);
          userCredential.setPassword(passwordEncoder.encode(defaultPassword));
          break;
      }
      this.userCredentialRepository.save(userCredential);
    } catch (Exception e) {
      throw new CommonRuntimeException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Error while creating user"
      );
    }
    return user;
  }

  UserTenant createUserTenantData(
    User user,
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

  public Optional<User> verifyPassword(String username, String password) {
    Optional<User> user =
      this.userRepository.findUserByUsername(username.toLowerCase());
    if (user.isEmpty() || user.get().getDeleted()) {
      throw new HttpServerErrorException(
        HttpStatus.UNAUTHORIZED,
        AuthenticateErrorKeys.USER_DOES_NOT_EXISTS.label
      );
    }
    Optional<UserCredential> userCredential =
      this.userCredentialRepository.findByUserId(user.get().getId());
    if (
      userCredential.isPresent() &&
      userCredential.get().getPassword().isEmpty() ||
      !Objects.equals(
        userCredential.get().getAuthProvider(),
        AuthProvider.INTERNAL.label
      ) ||
      !(BCrypt.checkpw(password, userCredential.get().getPassword()))
    ) {
      log.error("User credentials not found in DB or is invalid");
      throw new HttpServerErrorException(
        HttpStatus.UNAUTHORIZED,
        AuthErrorKeys.INVALID_CREDENTIALS.label
      );
    } else {
      return user;
    }
  }

  public UserTenant verifyClientUserLogin(
    LoginDto req,
    AuthClient client,
    User user
  ) {
    User currentUser = user;

    Optional<UserTenant> userTenant = userTenantRepository.findUserTenantByUserId(
      currentUser.getId()
    );
    UserStatus userStatus = userTenant.get().getStatus();

    if (currentUser.getAuthClientIds().size() == 0) {
      log.error("No allowed auth clients found for this user in DB");
      throw new HttpServerErrorException(
        HttpStatus.UNPROCESSABLE_ENTITY,
        AuthErrorKeys.CLIENT_USER_MISSING.label
      );
    } else if (!StringUtils.hasLength(req.getClientSecret())) {
      log.error("client secret key missing from request object");
      throw new HttpServerErrorException(
        HttpStatus.BAD_REQUEST,
        AuthErrorKeys.CLIENT_SECRET_MISSING.label
      );
    } else if (!currentUser.getAuthClientIds().contains(client.getId())) {
      log.error("User is not allowed to access client id passed in request");
      throw new HttpServerErrorException(
        HttpStatus.UNAUTHORIZED,
        AuthErrorKeys.CLIENT_INVALID.label
      );
    } else if (userStatus == UserStatus.REGISTERED) {
      log.error("User is in registered state");
      throw new HttpServerErrorException(
        HttpStatus.BAD_REQUEST,
        "User not active yet"
      );
    } else {
      // for sonar
    }
    return userTenant.get();
  }
}
