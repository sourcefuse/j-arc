package com.sourcefuse.jarc.services.usertenantservice.integration;

import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.services.usertenantservice.controller.TenantUserController;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserDto;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import com.sourcefuse.jarc.services.usertenantservice.mocks.JsonUtils;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockCurrentUserSession;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockTenantUser;
import com.sourcefuse.jarc.services.usertenantservice.service.DeleteTenantUserServiceImpl;
import com.sourcefuse.jarc.services.usertenantservice.service.TenantUserServiceImpl;
import com.sourcefuse.jarc.services.usertenantservice.service.UpdateTenantUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@DisplayName("TenantUserController Integration Tests")
class TenantUserControllerTests {

  private MockMvc mockMvc;

  @Mock
  private TenantUserServiceImpl tenantUserService;

  @Mock
  private UpdateTenantUserServiceImpl updateTenantUserService;

  @Mock
  private DeleteTenantUserServiceImpl deleteTenantUserService;

  @InjectMocks
  private TenantUserController tenantUserController;

  private String basePath = "/tenants/{id}/users";

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(tenantUserController).build();
    //Set Current LoggedIn User
    MockCurrentUserSession.setCurrentLoggedInUser(null, null, null);
  }

  @Test
  @DisplayName("Create User Tenants - Success -Integration")
  void testCreateUserTenants_Success() throws Exception {
    // Mock input data
    UserDto userDto = MockTenantUser.getUserDtoObj();

    // Mock options
    Map<String, String> options = new HashMap<>();
    options.put("authId", "123456");
    options.put("authProvider", "example-provider");
    // Mock the tenantUserService.create() method
    UserDto expectedUserDto = MockTenantUser.getUserDtoObj();
    Mockito
      .when(tenantUserService.create(userDto, null, options))
      .thenReturn(expectedUserDto);

    // Perform the request and assert the response
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath, userDto.getUserTenantId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(userDto))
      )
      .andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  @DisplayName("Test: Should pass with invalid tenant Id")
  void testCreate_InvalidInput_TenantId() throws Exception {
    // Mock input data
    UserDto userDto = MockTenantUser.getUserDtoObj();
    userDto.setTenantId(null);

    // Perform the request and assert the response
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath, userDto.getUserTenantId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(userDto))
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName("Test: Should pass with invalid Role Id")
  void testCreate_InvalidInput_RoleId() throws Exception {
    // Mock input data
    UserDto userDto = MockTenantUser.getUserDtoObj();
    userDto.setRoleId(null);

    // Perform the request and assert the response
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath, userDto.getUserTenantId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(userDto))
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName("Test: Should pass with invalid UserDetails ")
  void testCreate_InvalidInput_UserDetails() throws Exception {
    // Mock input data
    UserDto userDto = MockTenantUser.getUserDtoObj();
    userDto.setUserDetails(null);

    // Perform the request and assert the response
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath, userDto.getUserTenantId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(userDto))
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName(
    "Test: Should pass with invalid UserDetails First name blank or null"
  )
  void testCreate_InvalidInput_User_FirstName() throws Exception {
    // Mock input data
    UserDto userDto = MockTenantUser.getUserDtoObj();
    userDto.getUserDetails().setFirstName("");

    // Perform the request and assert the response
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath, userDto.getUserTenantId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(userDto))
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName(
    "Test: Should pass with invalid UserDetails user name blank or null"
  )
  void testCreate_InvalidInput_User_UserName() throws Exception {
    // Mock input data
    UserDto userDto = MockTenantUser.getUserDtoObj();
    userDto.getUserDetails().setUsername("");

    // Perform the request and assert the response
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath, userDto.getUserTenantId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(userDto))
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName("Test: Should pass with invalid UserDetails Email")
  void testCreate_InvalidInput_User_Email() throws Exception {
    // Mock input data
    UserDto userDto = MockTenantUser.getUserDtoObj();
    userDto.getUserDetails().setEmail("adil*!@gmail.com");

    // Perform the request and assert the response
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath, userDto.getUserTenantId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(userDto))
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName("Test: Should pass with invalid UserDetails Phone number")
  void testCreate_InvalidInput_User_Phone() throws Exception {
    // Mock input data
    UserDto userDto = MockTenantUser.getUserDtoObj();
    userDto.getUserDetails().setPhone("+091123456789");

    // Perform the request and assert the response
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath, userDto.getUserTenantId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(userDto))
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName("Test getUserTenantById - Success")
  void testGetUserTenantByIdSuccess() throws Exception {
    // Arrange
    List<UserDto> userDtoList = Collections.singletonList(
      MockTenantUser.getUserDtoObj()
    );
    UUID tenantId = MockTenantUser.TENANT_ID;

    MockCurrentUserSession.setCurrentLoggedInUser(tenantId, null, null);
    Mockito
      .when(tenantUserService.getUserView(ArgumentMatchers.any()))
      .thenReturn(userDtoList);

    // Act & Assert
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get(basePath, tenantId)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
  }

  @Test
  @DisplayName("Test getUserTenantById - forbidden")
  void testGetUserTenantByIdUnauthorized() throws Exception {
    // Arrange
    MockCurrentUserSession.setCurrentLoggedInUser(
      MockTenantUser.TENANT_ID,
      null,
      null
    );
    // Act & Assert
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get(basePath, MockTenantUser.INVALID_ID)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @DisplayName("Test getUserTenantById - Success")
  void testGetAllUserViews() throws Exception {
    // Arrange
    List<UserDto> userDtoList = Collections.singletonList(
      MockTenantUser.getUserDtoObj()
    );
    Mockito
      .when(tenantUserService.getAllUsers(ArgumentMatchers.any()))
      .thenReturn(userDtoList);

    // Act & Assert
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get(basePath + "/view-all", MockTenantUser.TENANT_ID)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
  }

  @Test
  @DisplayName("Test Count UserTenant - Success")
  void testCountTenantUser() throws Exception {
    // Arrange
    List<UserDto> userDtoList = Collections.singletonList(
      MockTenantUser.getUserDtoObj()
    );

    UUID tenantId = MockTenantUser.TENANT_ID;
    MockCurrentUserSession.setCurrentLoggedInUser(tenantId, null, null);
    Mockito
      .when(tenantUserService.getUserView(ArgumentMatchers.any()))
      .thenReturn(userDtoList);

    // Act & Assert
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get(basePath + "/count", tenantId)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1));
  }

  @Test
  @DisplayName("Test find All User by userId - Success")
  void testFindAllUserByUserId() throws Exception {
    // Arrange
    UUID tenantId = MockTenantUser.TENANT_ID;
    UUID userId = MockTenantUser.USER_ID;
    MockCurrentUserSession.setCurrentLoggedInUser(tenantId, userId, null);
    Mockito
      .when(tenantUserService.findById(ArgumentMatchers.any()))
      .thenReturn(MockTenantUser.getUserViewObj());

    // Act & Assert
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get(basePath + "/{userId}", tenantId, userId)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$").exists());
  }

  @Test
  @DisplayName("Test find All User by userId - forbidden")
  void testFindAllUserByUserIdForbidden() throws Exception {
    // Arrange
    UUID tenantId = MockTenantUser.TENANT_ID;
    UUID userId = MockTenantUser.USER_ID;

    MockCurrentUserSession.setCurrentLoggedInUser(
      tenantId,
      MockTenantUser.INVALID_ID,
      null
    );
    Mockito
      .when(tenantUserService.findById(ArgumentMatchers.any()))
      .thenReturn(MockTenantUser.getUserViewObj());

    // Act & Assert
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get(basePath + "/{userId}", tenantId, userId)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @DisplayName("Update User by ID - Success")
  void testUpdateUserByIdSuccess() throws Exception {
    // Arrange
    UUID id = MockTenantUser.TENANT_ID;
    UUID userId = MockTenantUser.USER_ID;
    UserView userView = MockTenantUser.getUserViewObj();
    userView.setUsername("new username");

    MockCurrentUserSession.setCurrentLoggedInUser(null, id, null);

   CurrentUser currentUser= MockCurrentUserSession.getCurrentUser();
    Mockito
      .doNothing()
      .when(updateTenantUserService)
      .updateById(
              currentUser,
        userId,
        userView,
        id
      );

    // Act & Assert
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .patch(basePath + "/{userId}", id, userId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(userView))
      )
      .andExpect(MockMvcResultMatchers.status().isNoContent())
      .andExpect(MockMvcResultMatchers.content().string("User PATCH success"));

    // Verify
    Mockito
      .verify(updateTenantUserService, Mockito.times(1))
      .updateById(
        currentUser,
        userId,
        userView,
        id
      );
  }

  @Test
  @DisplayName("Update User by ID - Forbidden ")
  void testUpdateUserByIdForbidden() throws Exception {
    // Arrange
    UUID id = MockTenantUser.TENANT_ID;
    UUID userId = MockTenantUser.USER_ID;
    UserView userView = MockTenantUser.getUserViewObj();
    userView.setUsername("new username");

    MockCurrentUserSession.setCurrentLoggedInUser(null, userId, null);
    CurrentUser currentUser= MockCurrentUserSession.getCurrentUser();

    Mockito
      .doNothing()
      .when(updateTenantUserService)
      .updateById(
        currentUser,
        userId,
        userView,
        id
      );

    // Act & Assert
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .patch(basePath + "/{userId}", id, userId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(userView))
      )
      .andExpect(MockMvcResultMatchers.status().isForbidden());


    // Verify
    Mockito
      .verify(updateTenantUserService, Mockito.never())
      .updateById(
        currentUser,
        userId,
        userView,
        id
      );
  }

  @Test
  @DisplayName("Delete User by ID - Success")
  void testDeleteUserByIdSuccess() throws Exception {
    // Arrange
    UUID tenantId = MockTenantUser.TENANT_ID;
    UUID userId = MockTenantUser.USER_ID;
    CurrentUser currentUser= MockCurrentUserSession.getCurrentUser();
    Mockito
      .doNothing()
      .when(deleteTenantUserService)
      .deleteUserById(
        currentUser,
        userId,
        tenantId
      );

    // Act & Assert
    mockMvc
      .perform(
        MockMvcRequestBuilders.delete(basePath + "/{userId}", tenantId, userId)
      )
      .andExpect(MockMvcResultMatchers.status().isNoContent())
      .andExpect(MockMvcResultMatchers.content().string("User DELETE success"));

    // Verify
    Mockito
      .verify(deleteTenantUserService, Mockito.times(1))
      .deleteUserById(
        currentUser,
        userId,
        tenantId
      );
  }
}
