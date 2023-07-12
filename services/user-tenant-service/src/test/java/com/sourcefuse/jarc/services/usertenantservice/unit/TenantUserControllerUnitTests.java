package com.sourcefuse.jarc.services.usertenantservice.unit;

import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.services.usertenantservice.dto.Role;
import com.sourcefuse.jarc.services.usertenantservice.dto.Tenant;
import com.sourcefuse.jarc.services.usertenantservice.dto.User;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserDto;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenant;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockCurrentUserSession;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockTenantUser;
import com.sourcefuse.jarc.services.usertenantservice.repository.AuthClientsRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.RoleRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserGroupsRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserTenantRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserViewRepository;
import com.sourcefuse.jarc.services.usertenantservice.service.DeleteTenantUserServiceImpl;
import com.sourcefuse.jarc.services.usertenantservice.service.TenantUserServiceImpl;
import com.sourcefuse.jarc.services.usertenantservice.service.UpdateTenantUserServiceImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@DisplayName("TenantUserController Unit Tests")
class TenantUserControllerUnitTests {

  @Mock
  private UserRepository userRepository;

  @Mock
  private RoleRepository roleRepository;

  @Mock
  private UserViewRepository userViewRepository;

  @Mock
  private UserTenantRepository userTenantRepository;

  @Mock
  private AuthClientsRepository authClientsRepository;

  private UserDto userDto;

  @InjectMocks
  private TenantUserServiceImpl tenantUserService;

  @InjectMocks
  private UpdateTenantUserServiceImpl updateTenantUserService;

  @InjectMocks
  private DeleteTenantUserServiceImpl deleteTenantUserService;

  @Mock
  private UserGroupsRepository userGroupsRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    userDto = MockTenantUser.getUserDtoObj();
    //Set Current LoggedIn User
    MockCurrentUserSession.setCurrentLoggedInUser(null, null, null);
  }

  @Test
  @DisplayName("Success :Create User and register")
  void testCreateAndRegisterUser() {
    // Arrange
    UserDto userData = userDto;
    MockCurrentUserSession.setCurrentLoggedInUser(
      userData.getTenantId(),
      null,
      null
    );
    Map<String, String> options = new HashMap<>();
    // Mock necessary repository methods
    Mockito
      .when(roleRepository.findById(userData.getRoleId()))
      .thenReturn(Optional.of(new Role()));
    Mockito
      .when(userRepository.findOne(ArgumentMatchers.any(Specification.class)))
      .thenReturn(Optional.empty());
    Mockito
      .when(
        authClientsRepository.findAll(ArgumentMatchers.any(Specification.class))
      )
      .thenReturn(Collections.emptyList());
    Mockito
      .when(userRepository.save(ArgumentMatchers.any(User.class)))
      .thenReturn(new User());
    Mockito
      .when(userTenantRepository.save(ArgumentMatchers.any()))
      .thenReturn(MockTenantUser.getUserTenantObj());

    // Act
    UserDto result = tenantUserService.create(
      userData,
      MockCurrentUserSession.getCurrentUser(),
      options
    );

    // Assert
    Assertions.assertNotNull(result);
    //verify save user method and save userTenant is called
    Mockito
      .verify(userRepository, Mockito.times(1))
      .save(ArgumentMatchers.any(User.class));
    Mockito
      .verify(userTenantRepository, Mockito.times(1))
      .save(ArgumentMatchers.any());
  }

  @Test
  @DisplayName(
    "User Already exits but not registered hence process for registration"
  )
  void testRegisterUser() {
    // Arrange
    UserDto userData = userDto;
    MockCurrentUserSession.setCurrentLoggedInUser(
      userData.getTenantId(),
      null,
      null
    );
    Map<String, String> options = new HashMap<>();
    // Mock necessary repository methods
    Mockito
      .when(roleRepository.findById(userData.getRoleId()))
      .thenReturn(Optional.of(new Role()));
    Mockito
      .when(userRepository.findOne(ArgumentMatchers.any(Specification.class)))
      .thenReturn(Optional.of(MockTenantUser.getUserObj()));
    Mockito
      .when(
        userTenantRepository.findOne(ArgumentMatchers.any(Specification.class))
      )
      .thenReturn(Optional.empty());
    Mockito
      .when(
        authClientsRepository.findAll(ArgumentMatchers.any(Specification.class))
      )
      .thenReturn(Collections.emptyList());
    Mockito
      .when(userRepository.save(ArgumentMatchers.any(User.class)))
      .thenReturn(new User());
    Mockito
      .when(userTenantRepository.save(ArgumentMatchers.any()))
      .thenReturn(MockTenantUser.getUserTenantObj());

    // Act
    UserDto result = tenantUserService.create(
      userData,
      MockCurrentUserSession.getCurrentUser(),
      options
    );

    // Assert
    Assertions.assertNotNull(result);

    //verify save user method is not called as it is already register
    Mockito
      .verify(userRepository, Mockito.never())
      .save(ArgumentMatchers.any(User.class));
  }

  @Test
  @DisplayName("User Already exits and Registered")
  void testUserExitsAndRegistered() {
    // Arrange
    UserDto userData = userDto;

    MockCurrentUserSession.setCurrentLoggedInUser(
      userData.getTenantId(),
      null,
      null
    );

    Map<String, String> options = new HashMap<>();
    // Mock necessary repository methods
    Mockito
      .when(roleRepository.findById(userData.getRoleId()))
      .thenReturn(Optional.of(new Role()));
    Mockito
      .when(userRepository.findOne(ArgumentMatchers.any(Specification.class)))
      .thenReturn(Optional.of(MockTenantUser.getUserObj()));
    Mockito
      .when(
        userTenantRepository.findOne(ArgumentMatchers.any(Specification.class))
      )
      .thenReturn(Optional.of(MockTenantUser.getUserTenantObj()));
    Mockito
      .when(
        authClientsRepository.findAll(ArgumentMatchers.any(Specification.class))
      )
      .thenReturn(Collections.emptyList());
    Mockito
      .when(userRepository.save(ArgumentMatchers.any(User.class)))
      .thenReturn(new User());
    Mockito
      .when(userTenantRepository.save(ArgumentMatchers.any()))
      .thenReturn(MockTenantUser.getUserTenantObj());
    CurrentUser currentUser = MockCurrentUserSession.getCurrentUser();
    // Act
    ResponseStatusException exception = Assertions.assertThrows(
      ResponseStatusException.class,
      () -> tenantUserService.create(userData, currentUser, options)
    );

    // Assert
    Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

    //verify save user and save userTenant method is
    // not called as it is already register
    Mockito
      .verify(userRepository, Mockito.never())
      .save(ArgumentMatchers.any(User.class));
    Mockito
      .verify(userTenantRepository, Mockito.never())
      .save(ArgumentMatchers.any());
  }

  @Test
  @DisplayName("Create User Forbidden")
  void testCreateUserForbidden() {
    // Arrange
    UserDto userData = userDto;
    MockCurrentUserSession.setCurrentLoggedInUser(
      MockTenantUser.INVALID_ID,
      null,
      null
    );
    Map<String, String> options = new HashMap<>();
    // Mock necessary repository methods
    // Act
    CurrentUser currentUser = MockCurrentUserSession.getCurrentUser();

    ResponseStatusException exception = Assertions.assertThrows(
      ResponseStatusException.class,
      () -> tenantUserService.create(userData, currentUser, options)
    );

    // Assert
    Assertions.assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());

    //verify save user method is not called
    Mockito
      .verify(userRepository, Mockito.never())
      .save(ArgumentMatchers.any(User.class));
    Mockito
      .verify(userTenantRepository, Mockito.never())
      .save(ArgumentMatchers.any());
  }

  @Test
  @DisplayName("Create User but Role not present")
  void testCreateUserRoleNotPresent() {
    // Arrange
    UserDto userData = userDto;
    MockCurrentUserSession.setCurrentLoggedInUser(
      userData.getTenantId(),
      null,
      null
    );
    Map<String, String> options = new HashMap<>();
    // Mock necessary repository methods
    Mockito
      .when(roleRepository.findById(userData.getRoleId()))
      .thenReturn(Optional.empty());
    // Act
    CurrentUser currentUser = MockCurrentUserSession.getCurrentUser();
    ResponseStatusException exception = Assertions.assertThrows(
      ResponseStatusException.class,
      () -> tenantUserService.create(userData, currentUser, options)
    );

    // Assert
    Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

    //verify save user method is not called
    Mockito
      .verify(userRepository, Mockito.never())
      .save(ArgumentMatchers.any(User.class));
    Mockito
      .verify(userTenantRepository, Mockito.never())
      .save(ArgumentMatchers.any());
  }

  @Test
  @DisplayName("Test getUserView - Success")
  void testGetUserViewSuccess() {
    // Arrange
    List<UserView> userViewsList = new ArrayList<>();
    userViewsList.add(MockTenantUser.getUserViewObj());
    userViewsList.add(MockTenantUser.getUserViewObj());

    Mockito
      .when(
        userViewRepository.findAll(ArgumentMatchers.any(Specification.class))
      )
      .thenReturn(userViewsList);

    MockCurrentUserSession.setCurrentLoggedInUser(
      MockTenantUser.TENANT_ID,
      null,
      null
    );
    // Act
    List<UserDto> result = tenantUserService.getUserView(
      MockTenantUser.TENANT_ID,
      MockCurrentUserSession.getCurrentUser()
    );

    // Assert
    Assertions.assertEquals(userViewsList.size(), result.size());
  }

  @Test
  @DisplayName(
    "Test getUserView - Forbidden :does not have VIEW_ANY_USER permission " +
    "and tenantId does not match with current users tenantId"
  )
  void testGetUserViewForbidden() {
    // Arrange
    List<UserView> userViewsList = new ArrayList<>();
    userViewsList.add(MockTenantUser.getUserViewObj());
    userViewsList.add(MockTenantUser.getUserViewObj());

    Mockito
      .when(
        userViewRepository.findAll(ArgumentMatchers.any(Specification.class))
      )
      .thenReturn(userViewsList);

    // Act
    ResponseStatusException exception = Assertions.assertThrows(
      ResponseStatusException.class,
      () ->
        tenantUserService.getUserView(
          MockTenantUser.TENANT_ID,
          MockCurrentUserSession.getCurrentUser()
        )
    );

    // Assert
    Assertions.assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
  }

  @Test
  @DisplayName("Test getUserView - Empty List")
  void testGetUserViewEmptyList() {
    // Arrange
    List<UserView> userViewsList = new ArrayList<>();
    Mockito
      .when(
        userViewRepository.findAll(ArgumentMatchers.any(Specification.class))
      )
      .thenReturn(userViewsList);
    MockCurrentUserSession.setCurrentLoggedInUser(
      MockTenantUser.TENANT_ID,
      null,
      null
    );
    // Act
    List<UserDto> result = tenantUserService.getUserView(
      MockTenantUser.TENANT_ID,
      MockCurrentUserSession.getCurrentUser()
    );

    // Assert
    Assertions.assertEquals(0, result.size());
  }

  @Test
  @DisplayName("Test get All UserView - Success")
  void testGetAllUserViewSuccess() {
    // Arrange
    List<UserView> userViewsList = new ArrayList<>();
    userViewsList.add(MockTenantUser.getUserViewObj());
    userViewsList.add(MockTenantUser.getUserViewObj());

    Mockito
      .when(
        userViewRepository.findAll(ArgumentMatchers.any(Specification.class))
      )
      .thenReturn(userViewsList);
    MockCurrentUserSession.setCurrentLoggedInUser(
      MockTenantUser.TENANT_ID,
      null,
      null
    );
    // Act
    List<UserDto> result = tenantUserService.getUserView(
      MockTenantUser.TENANT_ID,
      MockCurrentUserSession.getCurrentUser()
    );

    // Assert
    Assertions.assertNotNull(result);
    Assertions.assertEquals(userViewsList.size(), result.size());
  }

  @Test
  @DisplayName(
    "Test get All UserView - Forbidden  :does not have VIEW_ANY_USER permission" +
    "and tenantId does not match with current users tenantId"
  )
  void testGetAllUserViewForbidden() {
    // Arrange
    List<UserView> userViewsList = new ArrayList<>();
    userViewsList.add(MockTenantUser.getUserViewObj());
    userViewsList.add(MockTenantUser.getUserViewObj());

    Mockito
      .when(
        userViewRepository.findAll(ArgumentMatchers.any(Specification.class))
      )
      .thenReturn(userViewsList);

    // Act
    ResponseStatusException exception = Assertions.assertThrows(
      ResponseStatusException.class,
      () ->
        tenantUserService.getUserView(
          MockTenantUser.TENANT_ID,
          MockCurrentUserSession.getCurrentUser()
        )
    );

    // Assert
    Assertions.assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
  }

  @Test
  @DisplayName("Test get All UserView - Empty")
  void testGetAllUserViewEmpty() {
    // Arrange
    List<UserView> userViewsList = new ArrayList<>();

    Mockito
      .when(
        userViewRepository.findAll(ArgumentMatchers.any(Specification.class))
      )
      .thenReturn(userViewsList);
    MockCurrentUserSession.setCurrentLoggedInUser(
      MockTenantUser.TENANT_ID,
      null,
      null
    );

    // Act
    List<UserDto> result = tenantUserService.getUserView(
      MockTenantUser.TENANT_ID,
      MockCurrentUserSession.getCurrentUser()
    );

    // Assert
    Assertions.assertNotNull(result);
    Assertions.assertEquals(userViewsList.size(), result.size());
  }

  @Test
  @DisplayName("Test Count TenantUser/UserView  - Success")
  void testCountTenantUser() {
    // Arrange
    List<UserView> userViewsList = new ArrayList<>();
    userViewsList.add(MockTenantUser.getUserViewObj());
    userViewsList.add(MockTenantUser.getUserViewObj());

    Mockito
      .when(
        userViewRepository.findAll(ArgumentMatchers.any(Specification.class))
      )
      .thenReturn(userViewsList);
    MockCurrentUserSession.setCurrentLoggedInUser(
      MockTenantUser.TENANT_ID,
      null,
      null
    );
    // Act
    List<UserDto> result = tenantUserService.getUserView(
      MockTenantUser.TENANT_ID,
      MockCurrentUserSession.getCurrentUser()
    );

    // Assert
    Assertions.assertNotNull(result);
    Assertions.assertEquals(userViewsList.size(), result.size());
  }

  @Test
  @DisplayName("Test Count TenantUser/UserView  - Empty")
  void testCountTenantUserEmpty() {
    // Arrange
    List<UserView> userViewsList = new ArrayList<>();

    Mockito
      .when(
        userViewRepository.findAll(ArgumentMatchers.any(Specification.class))
      )
      .thenReturn(userViewsList);
    MockCurrentUserSession.setCurrentLoggedInUser(
      MockTenantUser.TENANT_ID,
      null,
      null
    );
    // Act
    List<UserDto> result = tenantUserService.getUserView(
      MockTenantUser.TENANT_ID,
      MockCurrentUserSession.getCurrentUser()
    );

    // Assert
    Assertions.assertNotNull(result);
    Assertions.assertEquals(userViewsList.size(), result.size());
  }

  @Test
  @DisplayName(
    "Test Count TenantUser/UserView  - forbidden:" +
    "does not have VIEW_ANY_USER permission and tenantId does not match with current users tenantId"
  )
  void testCountTenantUserForbidden() {
    // Arrange
    List<UserView> userViewsList = new ArrayList<>();
    userViewsList.add(MockTenantUser.getUserViewObj());
    userViewsList.add(MockTenantUser.getUserViewObj());

    Mockito
      .when(
        userViewRepository.findAll(ArgumentMatchers.any(Specification.class))
      )
      .thenReturn(userViewsList);
    MockCurrentUserSession.setCurrentLoggedInUser(
      MockTenantUser.INVALID_ID,
      null,
      null
    );
    // Act
    ResponseStatusException exception = Assertions.assertThrows(
      ResponseStatusException.class,
      () ->
        tenantUserService.getUserView(
          MockTenantUser.TENANT_ID,
          MockCurrentUserSession.getCurrentUser()
        )
    );

    // Assert
    Assertions.assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
  }

  @Test
  @DisplayName("Test findById - Success")
  void testFindByIdFound() {
    // Arrange
    UUID userId = MockTenantUser.USER_ID;
    UUID tenantId = MockTenantUser.TENANT_ID;
    UserView expectedUserView = MockTenantUser.getUserViewObj();

    Mockito
      .when(userViewRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.ofNullable(expectedUserView));
    MockCurrentUserSession.setCurrentLoggedInUser(tenantId, userId, null);

    // Act
    UserView actualUserView = tenantUserService.findById(
      userId,
      tenantId,
      MockCurrentUserSession.getCurrentUser()
    );

    // Assert
    Assertions.assertEquals(expectedUserView, actualUserView);
  }

  @Test
  @DisplayName("Test findById - Not Found")
  void testFindByIdNotFound() {
    // Arrange
    UUID userId = MockTenantUser.USER_ID;
    UUID tenantId = MockTenantUser.TENANT_ID;
    Mockito
      .when(userViewRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.empty());

    MockCurrentUserSession.setCurrentLoggedInUser(tenantId, userId, null);

    // Act & Assert
    ResponseStatusException exception = Assertions.assertThrows(
      ResponseStatusException.class,
      () ->
        tenantUserService.findById(
          userId,
          tenantId,
          MockCurrentUserSession.getCurrentUser()
        )
    );

    Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    Assertions.assertEquals(
      "No User view is present against given value",
      exception.getReason()
    );
  }

  @Test
  @DisplayName(
    "Test getUserTenantById - forbidden:" +
    "Has VIEW_OWN_USER permission but userId does not match with current users userId"
  )
  void testGetUserTenantByIdUnauthorized() throws Exception {
    UUID userId = MockTenantUser.USER_ID;
    UUID tenantId = MockTenantUser.TENANT_ID;

    // Arrange
    MockCurrentUserSession.setCurrentLoggedInUser(
      MockTenantUser.TENANT_ID,
      MockTenantUser.INVALID_ID,
      null
    );
    // Act & Assert
    ResponseStatusException exception = Assertions.assertThrows(
      ResponseStatusException.class,
      () ->
        tenantUserService.findById(
          userId,
          tenantId,
          MockCurrentUserSession.getCurrentUser()
        )
    );
    Assertions.assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
  }

  @Test
  @DisplayName("Update User By ID - Success")
  void testUpdateUserByIdSuccess() {
    UUID userId = MockTenantUser.USER_ID;
    UUID tenantId = MockTenantUser.TENANT_ID;

    MockCurrentUserSession.setCurrentLoggedInUser(tenantId, userId, null);

    User user = MockTenantUser.getUserObj();
    user.setId(userId);
    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    UserTenant userTenant = MockTenantUser.getUserTenantObj();
    userTenant.setTenant(new Tenant(tenantId));
    userTenant.setUser(new User(userId));

    Mockito
      .when(
        userTenantRepository.findOne(ArgumentMatchers.any(Specification.class))
      )
      .thenReturn(Optional.of(userTenant));
    Mockito
      .when(
        userTenantRepository.findAll(ArgumentMatchers.any(Specification.class))
      )
      .thenReturn(Collections.singletonList(userTenant));

    UserView userView = MockTenantUser.getUserViewObj();
    userView.setUsername("new username");
    updateTenantUserService.updateById(
      MockCurrentUserSession.getCurrentUser(),
      userId,
      userView,
      tenantId
    );

    // Add assertions to validate the expected behavior
    // For example, we can assert that the user's username, role, and status have been updated correctly
    Assertions.assertEquals("new username", user.getUsername());
    Assertions.assertEquals(userView.getRoleId(), userTenant.getRole().getId());
    Assertions.assertEquals(userView.getStatus(), userTenant.getStatus());
  }

  @Test
  @DisplayName(
    "Update User By ID - has UPDATE_OWN_USER permission " +
    "but CurrentUser UserId does not match with path variable UserId"
  )
  void testUpdateUserByIdInvalidUserId() {
    UUID userId = MockTenantUser.USER_ID;
    UUID tenantId = MockTenantUser.TENANT_ID;

    MockCurrentUserSession.setCurrentLoggedInUser(
      MockTenantUser.TENANT_ID,
      MockTenantUser.INVALID_ID,
      null
    );

    User user = MockTenantUser.getUserObj();
    user.setId(userId);
    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    UserTenant userTenant = MockTenantUser.getUserTenantObj();
    userTenant.setTenant(new Tenant(tenantId));
    userTenant.setUser(new User(userId));

    Mockito
      .when(
        userTenantRepository.findOne(ArgumentMatchers.any(Specification.class))
      )
      .thenReturn(Optional.of(userTenant));

    UserView userView = MockTenantUser.getUserViewObj();
    userView.setUsername("new username");

    CurrentUser currentUser = MockCurrentUserSession.getCurrentUser();

    ResponseStatusException exception = Assertions.assertThrows(
      ResponseStatusException.class,
      () ->
        updateTenantUserService.updateById(
          currentUser,
          userId,
          userView,
          tenantId
        )
    );

    // Add assertions to validate the expected behavior
    Assertions.assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
  }

  @Test
  @DisplayName(
    "Update User By ID - does not have UPDATE_ANY_USER permission " +
    "and CurrentUser TenantId does not match with path variable TenantId  "
  )
  void testUpdateUserByIdInvalidTenantId() {
    UUID userId = MockTenantUser.USER_ID;
    UUID tenantId = MockTenantUser.TENANT_ID;
    MockCurrentUserSession.setCurrentLoggedInUser(
      tenantId,
      MockTenantUser.INVALID_ID,
      null
    );

    User user = MockTenantUser.getUserObj();
    user.setId(userId);
    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    UserTenant userTenant = MockTenantUser.getUserTenantObj();
    userTenant.setTenant(new Tenant(tenantId));
    userTenant.setUser(new User(userId));

    Mockito
      .when(
        userTenantRepository.findOne(ArgumentMatchers.any(Specification.class))
      )
      .thenReturn(Optional.of(userTenant));

    UserView userView = MockTenantUser.getUserViewObj();
    userView.setUsername("new username");

    CurrentUser currentUser = MockCurrentUserSession.getCurrentUser();

    ResponseStatusException exception = Assertions.assertThrows(
      ResponseStatusException.class,
      () ->
        updateTenantUserService.updateById(
          currentUser,
          userId,
          userView,
          tenantId
        )
    );

    // Add assertions to validate the expected behavior
    Assertions.assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
  }

  @Test
  @DisplayName(
    "Update User By ID - UserTenant does not exits against userId and TenantId"
  )
  void testUpdateUserByIdUserTenantNotPresent() {
    UUID userId = MockTenantUser.USER_ID;
    UUID tenantId = MockTenantUser.TENANT_ID;

    MockCurrentUserSession.setCurrentLoggedInUser(
      tenantId,
      MockTenantUser.INVALID_ID,
      null
    );

    User user = MockTenantUser.getUserObj();
    user.setId(userId);
    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    Mockito
      .when(
        userTenantRepository.findOne(ArgumentMatchers.any(Specification.class))
      )
      .thenReturn(Optional.empty());

    UserView userView = MockTenantUser.getUserViewObj();
    userView.setUsername("new username");

    CurrentUser currentUser = MockCurrentUserSession.getCurrentUser();

    ResponseStatusException exception = Assertions.assertThrows(
      ResponseStatusException.class,
      () ->
        updateTenantUserService.updateById(
          currentUser,
          userId,
          userView,
          tenantId
        )
    );

    // Add assertions to validate the expected behavior
    Assertions.assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
  }

  @Test
  @DisplayName("Update User By ID - UserName already exits")
  void testUpdateUserByIdUserNameExits() {
    UUID userId = MockTenantUser.USER_ID;
    UUID tenantId = MockTenantUser.TENANT_ID;

    MockCurrentUserSession.setCurrentLoggedInUser(tenantId, userId, null);

    User user = MockTenantUser.getUserObj();
    user.setId(userId);
    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    UserTenant userTenant = MockTenantUser.getUserTenantObj();
    userTenant.setTenant(new Tenant(tenantId));
    userTenant.setUser(new User(userId));

    Mockito
      .when(
        userTenantRepository.findOne(ArgumentMatchers.any(Specification.class))
      )
      .thenReturn(Optional.of(userTenant));
    Mockito
      .when(
        userTenantRepository.findAll(ArgumentMatchers.any(Specification.class))
      )
      .thenReturn(Collections.singletonList(userTenant));
    Mockito
      .when(userRepository.count(ArgumentMatchers.any(Specification.class)))
      .thenReturn(1L);

    UserView userView = MockTenantUser.getUserViewObj();
    userView.setUsername("new username");

    CurrentUser currentUser = MockCurrentUserSession.getCurrentUser();

    ResponseStatusException exception = Assertions.assertThrows(
      ResponseStatusException.class,
      () ->
        updateTenantUserService.updateById(
          currentUser,
          userId,
          userView,
          tenantId
        )
    );

    // Add assertions to validate the expected behavior
    Assertions.assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
  }

  @Test
  @DisplayName("Success :Delete User By Id - User Tenant Exists")
  void testDeleteUserById_UserTenantExists() {
    // Arrange
    UUID userId = MockTenantUser.USER_ID;
    UUID tenantId = MockTenantUser.TENANT_ID;

    MockCurrentUserSession.setCurrentLoggedInUser(tenantId, userId, null);

    UserTenant userTenant = MockTenantUser.getUserTenantObj();

    Mockito
      .when(
        userTenantRepository.findAll(ArgumentMatchers.any(Specification.class))
      )
      .thenReturn(Collections.singletonList(MockTenantUser.getUserTenantObj()));
    Mockito
      .when(
        userTenantRepository.findOne(ArgumentMatchers.any(Specification.class))
      )
      .thenReturn(Optional.of(userTenant));
    Mockito
      .when(userRepository.findById(ArgumentMatchers.any()))
      .thenReturn(Optional.of(MockTenantUser.getUserObj()));
    // Act
    deleteTenantUserService.deleteUserById(
      MockCurrentUserSession.getCurrentUser(),
      userId,
      tenantId
    );

    // Assert
    Mockito
      .verify(userGroupsRepository, Mockito.times(1))
      .delete(ArgumentMatchers.any(Specification.class));
    Mockito
      .verify(userTenantRepository, Mockito.times(1))
      .delete(ArgumentMatchers.any(Specification.class));
    Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
    Mockito
      .verify(userRepository, Mockito.times(1))
      .save(ArgumentMatchers.any());
  }

  @Test
  @DisplayName("Delete User By Id - User Tenant Not Found")
  void testDeleteUserById_UserTenantNotFound() {
    // Arrange
    UUID userId = MockTenantUser.USER_ID;
    UUID tenantId = MockTenantUser.TENANT_ID;
    MockCurrentUserSession.setCurrentLoggedInUser(tenantId, userId, null);
    Mockito
      .when(
        userTenantRepository.findOne(ArgumentMatchers.any(Specification.class))
      )
      .thenReturn(Optional.empty());

    CurrentUser currentUser = MockCurrentUserSession.getCurrentUser();

    // Act & Assert
    ResponseStatusException exception = Assertions.assertThrows(
      ResponseStatusException.class,
      () ->
        deleteTenantUserService.deleteUserById(currentUser, userId, tenantId)
    );
    Assertions.assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    Assertions.assertEquals("NOT_ALLOWED_ACCESS", exception.getReason());

    Mockito
      .verify(userGroupsRepository, Mockito.never())
      .delete(ArgumentMatchers.any(Specification.class));
    Mockito
      .verify(userTenantRepository, Mockito.never())
      .delete(ArgumentMatchers.any(Specification.class));
    Mockito
      .verify(userRepository, Mockito.never())
      .findById(ArgumentMatchers.any());
    Mockito
      .verify(userRepository, Mockito.never())
      .save(ArgumentMatchers.any());
  }

  @Test
  @DisplayName(
    "Delete User By Id - User Not Present hence defaultId not updated"
  )
  void testDeleteUserById_UserNotPresent() {
    // Arrange
    UUID userId = MockTenantUser.USER_ID;
    UUID tenantId = MockTenantUser.TENANT_ID;
    MockCurrentUserSession.setCurrentLoggedInUser(tenantId, userId, null);

    UserTenant userTenant = MockTenantUser.getUserTenantObj();

    Mockito
      .when(
        userTenantRepository.findOne(ArgumentMatchers.any(Specification.class))
      )
      .thenReturn(Optional.of(userTenant));
    Mockito
      .when(userRepository.findById(ArgumentMatchers.any()))
      .thenReturn(Optional.empty());
    // Act
    deleteTenantUserService.deleteUserById(
      MockCurrentUserSession.getCurrentUser(),
      userId,
      tenantId
    );

    // Assert
    Mockito
      .verify(userGroupsRepository, Mockito.times(1))
      .delete(ArgumentMatchers.any(Specification.class));
    Mockito
      .verify(userTenantRepository, Mockito.times(1))
      .delete(ArgumentMatchers.any(Specification.class));
    Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
    Mockito
      .verify(userRepository, Mockito.never())
      .save(ArgumentMatchers.any());
  }

  @Test
  @DisplayName(
    "Delete User By Id - does not have DELETE_ANY_USER permission and " +
    "Tenant ID does not match with current users tenantId"
  )
  void testDeleteUserById_InvalidTenantId() {
    // Arrange
    UUID userId = MockTenantUser.USER_ID;
    UUID tenantId = MockTenantUser.TENANT_ID;

    MockCurrentUserSession.setCurrentLoggedInUser(
      MockTenantUser.INVALID_ID,
      userId,
      null
    );
    CurrentUser currentUser = MockCurrentUserSession.getCurrentUser();

    // Act & Assert
    ResponseStatusException exception = Assertions.assertThrows(
      ResponseStatusException.class,
      () ->
        deleteTenantUserService.deleteUserById(currentUser, userId, tenantId)
    );
    Assertions.assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    Assertions.assertEquals("NOT_ALLOWED_ACCESS", exception.getReason());

    Mockito
      .verify(userGroupsRepository, Mockito.never())
      .delete(ArgumentMatchers.any(Specification.class));
    Mockito
      .verify(userTenantRepository, Mockito.never())
      .delete(ArgumentMatchers.any(Specification.class));
    Mockito
      .verify(userRepository, Mockito.never())
      .findById(ArgumentMatchers.any());
    Mockito
      .verify(userRepository, Mockito.never())
      .save(ArgumentMatchers.any());
  }
}
