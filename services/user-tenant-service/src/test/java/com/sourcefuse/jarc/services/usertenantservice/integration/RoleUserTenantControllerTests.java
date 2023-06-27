package com.sourcefuse.jarc.services.usertenantservice.integration;

import com.sourcefuse.jarc.core.constants.CommonConstants;
import com.sourcefuse.jarc.services.usertenantservice.controller.RoleUserTenantController;
import com.sourcefuse.jarc.services.usertenantservice.dto.Role;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenant;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockCurrentUserSession;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockRole;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockTenantUser;
import com.sourcefuse.jarc.services.usertenantservice.mocks.JsonUtils;
import com.sourcefuse.jarc.services.usertenantservice.repository.RoleRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.RoleUserTenantRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

@DisplayName("Create Role User Tenants Apis Integration/units Tests")
@ExtendWith(MockitoExtension.class)
class RoleUserTenantControllerTests {

  @Mock
  private RoleRepository roleRepository;

  @Mock
  private RoleUserTenantRepository roleUserTenantRepository;

  @InjectMocks
  private RoleUserTenantController roleUserTenantController;

  private Role role;
  private UUID mockRoleId;
  private UserTenant mockUserTenant;
  private MockMvc mockMvc;

  private String basePath = "/roles/{id}/user-tenants";

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(roleUserTenantController).build();

    //prepare the test data
    role = MockRole.getRoleObj();

    mockRoleId = MockRole.ROLE_ID;

    mockUserTenant = MockTenantUser.getUserTenantObj();
    mockUserTenant.setRole(role);
    mockUserTenant.setId(MockTenantUser.USER_TENANT_ID);
    //Set Current LoggedIn User
    MockCurrentUserSession.setCurrentLoggedInUser(null, null, null);
  }

  @Test
  @DisplayName("Create Role - Success")
  void testCreateRole_Success() throws Exception {
    // Mocked role
    Role mockRole = this.role;

    // Mocking repository behavior
    Mockito
      .when(roleRepository.findById(ArgumentMatchers.any(UUID.class)))
      .thenReturn(Optional.of(mockRole));
    Mockito
      .when(
        roleUserTenantRepository.save(ArgumentMatchers.any(UserTenant.class))
      )
      .thenReturn(mockUserTenant);

    // Call the method being tested
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath, mockRoleId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(mockUserTenant))
      )
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(
        MockMvcResultMatchers
          .jsonPath("$.id")
          .value(mockUserTenant.getId().toString())
      )
      .andExpect(
        MockMvcResultMatchers
          .jsonPath("$.status")
          .value(mockUserTenant.getStatus().toString())
      );

    // Verify repository method invocations
    Mockito
      .verify(roleRepository, Mockito.times(1))
      .findById(ArgumentMatchers.any(UUID.class));
    Mockito
      .verify(roleUserTenantRepository, Mockito.times(1))
      .save(ArgumentMatchers.any(UserTenant.class));
  }

  @Test
  @DisplayName("Create Role-UserTenant - Role Not Found")
  void testCreateRole_RoleNotFound() throws Exception {
    // Mocking repository behavior
    Mockito
      .when(roleRepository.findById(ArgumentMatchers.any(UUID.class)))
      .thenReturn(Optional.empty());

    // Call the method being tested and assert that it throws the expected exception
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath, mockRoleId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(mockUserTenant))
      )
      .andExpect(MockMvcResultMatchers.status().isNotFound())
      .andExpect(result ->
        Assertions.assertEquals(
          HttpStatus.NOT_FOUND.value(),
          result.getResponse().getStatus()
        )
      )
      .andExpect(result ->
        Assertions.assertEquals(
          CommonConstants.NO_ROLE_PRESENT,
          result.getResponse().getErrorMessage()
        )
      );

    // Verify repository method invocations
    Mockito
      .verify(roleRepository, Mockito.times(1))
      .findById(ArgumentMatchers.any(UUID.class));
    Mockito
      .verify(roleUserTenantRepository, Mockito.never())
      .save(ArgumentMatchers.any(UserTenant.class));
  }

  @Test
  @DisplayName("Test: should pass : UserTenant with null tenantId")
  void testUserTenant_Invalid_TenantId() throws Exception {
    // Prepare test data with invalid input
    mockUserTenant.setTnt(null);
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath, mockRoleId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(mockUserTenant))
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(result ->
        Assertions.assertTrue(
          result.getResolvedException() instanceof MethodArgumentNotValidException
        )
      );

    Mockito
      .verify(roleRepository, Mockito.never())
      .findById(ArgumentMatchers.any(UUID.class));
    Mockito
      .verify(roleUserTenantRepository, Mockito.never())
      .save(ArgumentMatchers.any(UserTenant.class));
  }

  @Test
  @DisplayName("Test: UserTenant with null Role ID")
  void testUserTenant_Invalid_RoleID() throws Exception {
    // Prepare test data with invalid input
    mockUserTenant.setRol(null);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath, mockRoleId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(mockUserTenant))
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(result ->
        Assertions.assertTrue(
          result.getResolvedException() instanceof MethodArgumentNotValidException
        )
      );

    Mockito
      .verify(roleRepository, Mockito.never())
      .findById(ArgumentMatchers.any(UUID.class));
    Mockito
      .verify(roleUserTenantRepository, Mockito.never())
      .save(ArgumentMatchers.any(UserTenant.class));
  }

  @Test
  @DisplayName("Test: UserTenant with null User ID")
  void testUserTenant_Invalid_UserID() throws Exception {
    // Prepare test data with invalid input
    mockUserTenant.setUsr(null);
    // Perform validation manually
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath, mockRoleId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(mockUserTenant))
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(result ->
        Assertions.assertTrue(
          result.getResolvedException() instanceof MethodArgumentNotValidException
        )
      );

    Mockito
      .verify(roleRepository, Mockito.never())
      .findById(ArgumentMatchers.any(UUID.class));
    Mockito
      .verify(roleUserTenantRepository, Mockito.never())
      .save(ArgumentMatchers.any(UserTenant.class));
  }

  @Test
  @DisplayName("Get All User Tenants By Role - Success")
  void testGetAllUserTenantByRole_Success() throws Exception {
    // Arrange
    UUID roleId = mockRoleId;
    List<UserTenant> expectedTenantList = Arrays.asList(
      new UserTenant(),
      new UserTenant()
    );

    Mockito
      .when(roleUserTenantRepository.findAll(Mockito.any(Specification.class)))
      .thenReturn(expectedTenantList);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get(basePath, mockRoleId)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(result -> Assertions.assertNotNull(result.getResponse()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2));

    Mockito
      .verify(roleUserTenantRepository, Mockito.times(1))
      .findAll(Mockito.any(Specification.class));
  }

  @Test
  @DisplayName("Get All User Tenants By Role - Empty Response")
  void testGetAllUserTenantByRole_Empty() throws Exception {
    // Arrange
    List<UserTenant> expectedTenantList = Arrays.asList();

    Mockito
      .when(roleUserTenantRepository.findAll(Mockito.any(Specification.class)))
      .thenReturn(expectedTenantList);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get(basePath, mockRoleId)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(result -> Assertions.assertNotNull(result.getResponse()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(0));

    Mockito
      .verify(roleUserTenantRepository, Mockito.times(1))
      .findAll(Mockito.any(Specification.class));
  }

  @Test
  @DisplayName("Test: case for count success")
  void testCount_Success() throws Exception {
    // Mock the behavior of roleRepository.count()
    Mockito
      .when(roleUserTenantRepository.findAll(Mockito.any(Specification.class)))
      .thenReturn(Arrays.asList(new UserTenant(), new UserTenant())); // Mock a count of 2

    // Perform the API call
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get(basePath + "/count", mockRoleId)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(result -> Assertions.assertNotNull(result.getResponse()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(2));

    // Verify that roleRepository.count() was called
    Mockito
      .verify(roleUserTenantRepository)
      .findAll(Mockito.any(Specification.class));
  }

  @Test
  @DisplayName("Test case should pass for 0 count")
  void testCount_Empty() throws Exception {
    // Mock the behavior of roleRepository.count()
    Mockito
      .when(roleUserTenantRepository.findAll(Mockito.any(Specification.class)))
      .thenReturn(Arrays.asList()); // Mock a count of 0

    // Perform the API call
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get(basePath + "/count", mockRoleId)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(result -> Assertions.assertNotNull(result.getResponse()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(0));

    // Verify that roleRepository.count() was called
    Mockito
      .verify(roleUserTenantRepository, Mockito.times(1))
      .findAll(Mockito.any(Specification.class));
  }

  @Test
  @DisplayName("Update All - Success")
  void testUpdateAll_Success() throws Exception {
    UserTenant sourceUserTenant = this.mockUserTenant;

    List<UserTenant> userTenantArrayList = Arrays.asList(
      new UserTenant(),
      new UserTenant()
    );

    Mockito
      .when(roleUserTenantRepository.findAll(Mockito.any(Specification.class)))
      .thenReturn(userTenantArrayList);
    Mockito
      .when(roleUserTenantRepository.saveAll(ArgumentMatchers.anyList()))
      .thenReturn(userTenantArrayList);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .patch(basePath, mockRoleId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(sourceUserTenant))
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(result -> Assertions.assertNotNull(result.getResponse()))
      .andExpect(
        MockMvcResultMatchers
          .jsonPath("$.count")
          .value(userTenantArrayList.size())
      );

    Mockito
      .verify(roleUserTenantRepository, Mockito.times(1))
      .findAll(Mockito.any(Specification.class));
    Mockito
      .verify(roleUserTenantRepository, Mockito.times(1))
      .saveAll(ArgumentMatchers.anyList());
  }

  @Test
  @DisplayName("Test Update  - Non-Existing UserTenant - Not Found")
  void testUpdate_NonExisting_UserTenant() throws Exception {
    // Arrange
    UUID nonExistingGroupId = mockRoleId;
    UserTenant sourceUserTenant = this.mockUserTenant;

    Mockito
      .when(roleUserTenantRepository.findAll(Mockito.any(Specification.class)))
      .thenReturn(new ArrayList());
    // Act & Assert
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .patch(basePath, mockRoleId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(sourceUserTenant))
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(result -> Assertions.assertNotNull(result.getResponse()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(0));

    Mockito
      .verify(roleUserTenantRepository, Mockito.times(1))
      .findAll(Mockito.any(Specification.class));
    Mockito
      .verify(roleUserTenantRepository, Mockito.never())
      .saveAll(Arrays.asList(new UserTenant()));
  }

  @Test
  @DisplayName("Test:Delete existing Role UserTenant by Id")
  void testDeleteRoleById_Success() throws Exception {
    Mockito
      .when(roleUserTenantRepository.delete(Mockito.any(Specification.class)))
      .thenReturn(2L);
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .delete(basePath, mockRoleId)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(2));

    Mockito
      .verify(roleUserTenantRepository, Mockito.times(1))
      .delete(Mockito.any(Specification.class));
  }

  @Test
  @DisplayName("Delete non-existing roles UserTenant should return 404 status")
  void testDeleteRolesById_NotFound() throws Exception {
    // Mock the behavior of dependencies
    Mockito
      .doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
      .when(roleUserTenantRepository)
      .delete(Mockito.any(Specification.class));

    // Call the method under test
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .delete("/roles/{id}/user-tenants", mockRoleId)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isNotFound());

    Mockito
      .verify(roleUserTenantRepository)
      .delete(Mockito.any(Specification.class));
  }
}
