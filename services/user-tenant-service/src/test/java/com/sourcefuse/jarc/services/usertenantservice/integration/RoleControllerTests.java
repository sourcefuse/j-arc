package com.sourcefuse.jarc.services.usertenantservice.integration;

import com.sourcefuse.jarc.core.constants.CommonConstants;
import com.sourcefuse.jarc.services.usertenantservice.controller.RoleController;
import com.sourcefuse.jarc.services.usertenantservice.dto.Role;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockCurrentUserSession;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockRole;
import com.sourcefuse.jarc.services.usertenantservice.mocks.JsonUtils;
import com.sourcefuse.jarc.services.usertenantservice.repository.RoleRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

@DisplayName("Create ROle Apis Integration/units Tests")
@ExtendWith(MockitoExtension.class)
class RoleControllerTests {

  @Mock
  private RoleRepository roleRepository;

  @InjectMocks
  private RoleController roleController;

  private Role role;

  private MockMvc mockMvc;

  private String basePath = "/roles";

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();

    //prepare the test data
    role = MockRole.getRoleObj();
    //Set Current LoggedIn User
    MockCurrentUserSession.setCurrentLoggedInUser(null, null, null);
  }

  @Test
  @DisplayName("Test:Should create the role")
  void testCreateRole_Success() throws Exception {
    Role savedRole = role;

    // Mock the behavior of roleRepository.save()
    Mockito.when(roleRepository.save(role)).thenReturn(savedRole);

    // Perform the API call
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(role))
      )
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(
        MockMvcResultMatchers
          .jsonPath("$.id")
          .value(savedRole.getId().toString())
      )
      .andExpect(
        MockMvcResultMatchers.jsonPath("$.name").value(savedRole.getName())
      );

    // Verify that roleRepository.save() was called
    Mockito.verify(roleRepository).save(role);
  }

  @Test
  @DisplayName("Test: Should pass with invalid name")
  void testCreateRole_InvalidInput_EmptyName() throws Exception {
    // Prepare test data with invalid input
    role.setName("");
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(role))
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(result ->
        Assertions.assertTrue(
          result.getResolvedException() instanceof MethodArgumentNotValidException
        )
      );

    Mockito.verify(roleRepository, Mockito.never()).save(role);
  }

  @Test
  @DisplayName("Test: Should pass with invalid RoleType")
  void testCreateRole_InvalidInput_EmptyRole() throws Exception {
    // Prepare test data with invalid input
    role.setRoleType(null);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(role))
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(result ->
        Assertions.assertTrue(
          result.getResolvedException() instanceof MethodArgumentNotValidException
        )
      );

    Mockito.verify(roleRepository, Mockito.never()).save(role);
  }

  @Test
  @DisplayName("Test: case for count success")
  void testCount_Success() throws Exception {
    // Mock the behavior of roleRepository.count()
    Mockito.when(roleRepository.count()).thenReturn(5L); // Mock a count of 5

    // Perform the API call
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get(basePath + "/count")
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(result -> Assertions.assertNotNull(result.getResponse()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(5));

    // Verify that roleRepository.count() was called
    Mockito.verify(roleRepository).count();
  }

  @Test
  @DisplayName("Test case should pass for 0 count")
  void testCount_Empty() throws Exception {
    // Mock the behavior of roleRepository.count()
    Mockito.when(roleRepository.count()).thenReturn(0L); // Mock a count of 0

    // Perform the API call
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get(basePath + "/count")
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(result -> Assertions.assertNotNull(result.getResponse()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(0));

    // Verify that roleRepository.count() was called
    Mockito.verify(roleRepository).count();
  }

  @Test
  @DisplayName("Test getAllRoles success")
  void testGetAllRoles_Success() throws Exception {
    // Prepare test data
    List<Role> roles = Arrays.asList(new Role(), new Role(), new Role());

    // Mock the behavior of roleRepository.findAll()
    Mockito.when(roleRepository.findAll()).thenReturn(roles);

    // Perform the API call
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get(basePath)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(result -> Assertions.assertNotNull(result.getResponse()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(3));

    // Verify that roleRepository.findAll() was called
    Mockito.verify(roleRepository).findAll();
  }

  @Test
  @DisplayName("Test: getAllRoles Empty Response")
  void testGetAllRoles_Empty() throws Exception {
    // Mock the behavior of roleRepository.findAll()
    Mockito.when(roleRepository.findAll()).thenReturn(Arrays.asList());

    // Perform the API call
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get(basePath)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(result -> Assertions.assertNotNull(result.getResponse()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(0));

    // Verify that roleRepository.findAll() was called
    Mockito.verify(roleRepository).findAll();
  }

  @Test
  @DisplayName("Test updateAll success")
  void testUpdateAll_Success() throws Exception {
    // Prepare test data
    role.setName("Update Name");

    List<Role> targetListRole = Arrays.asList(
      new Role(),
      new Role(),
      new Role()
    );

    // Mock the behavior of roleRepository.findAll()
    Mockito.when(roleRepository.findAll()).thenReturn(targetListRole);

    // Mock the behavior of roleRepository.saveAll()
    Mockito
      .when(roleRepository.saveAll(ArgumentMatchers.any()))
      .thenReturn(targetListRole);

    // Perform the API call
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .patch(basePath)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(role))
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(result -> Assertions.assertNotNull(result.getResponse()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(3));

    // Verify that roleRepository.findAll() was called
    Mockito.verify(roleRepository).findAll();

    // Verify that roleRepository.saveAll() was called with the updated roles
    Mockito.verify(roleRepository).saveAll(targetListRole);
  }

  @Test
  @DisplayName("Test: updateAll against No existing records found")
  void testUpdateAll_Empty() throws Exception {
    role.setName("Updated Name");

    // Mock the behavior of roleRepository.findAll()
    Mockito.when(roleRepository.findAll()).thenReturn(new ArrayList<>());

    // Perform the API call
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .patch(basePath)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(role))
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(result -> Assertions.assertNotNull(result.getResponse()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(0));

    // Verify that roleRepository.findAll() was called
    Mockito.verify(roleRepository).findAll();

    // Verify that roleRepository.saveAll() was not called
    Mockito
      .verify(roleRepository, Mockito.never())
      .saveAll(ArgumentMatchers.any());
  }

  @Test
  @DisplayName("Test: GetRoleByID with existing role")
  void testGetRoleByID_ExistingRole() throws Exception {
    // Mock the behavior of roleRepository.findById()
    UUID roleId = MockRole.ROLE_ID;
    Mockito.when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

    // Perform the API call
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get(basePath + "/{id}", roleId)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(
        MockMvcResultMatchers.jsonPath("$.id").value(role.getId().toString())
      )
      .andExpect(
        MockMvcResultMatchers.jsonPath("$.name").value(role.getName())
      );

    // Verify that roleRepository.findById() was called
    Mockito.verify(roleRepository).findById(roleId);
  }

  @Test
  @DisplayName("Test: getRoleByID with non-existing role")
  void testGetRoleByID_NonExistingRole() throws Exception {
    // Mock the behavior of roleRepository.findById()
    UUID roleId = MockRole.ROLE_ID;
    Mockito.when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

    // Perform the API call and catch the exception
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get(basePath + "/{id}", roleId)
          .contentType(MediaType.APPLICATION_JSON)
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

    // Verify that roleRepository.findById() was called
    Mockito.verify(roleRepository).findById(roleId);
  }

  @Test
  @DisplayName("Test Update Role - Existing Role - Success")
  void testUpdateRole_ExistingRole_Success() throws Exception {
    // Arrange
    UUID existingRoleId = MockRole.ROLE_ID;
    Role existingRole = new Role(existingRoleId);
    Role sourceRole = this.role;

    Mockito
      .when(roleRepository.findById(existingRoleId))
      .thenReturn(Optional.of(existingRole));
    Mockito.when(roleRepository.save(existingRole)).thenReturn(existingRole);

    // Act
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .patch(basePath + "/{id}", existingRoleId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(sourceRole))
      )
      .andExpect(MockMvcResultMatchers.status().isNoContent())
      .andExpect(
        MockMvcResultMatchers.jsonPath("$").value("Role PATCH success")
      );

    // Additional assertions
    Mockito.verify(roleRepository, Mockito.times(1)).findById(existingRoleId);
    Mockito.verify(roleRepository, Mockito.times(1)).save(existingRole);
  }

  @Test
  @DisplayName("Test Update Role - Non-Existing Role - Not Found")
  void testUpdateRole_NonExistingRole_NotFound() throws Exception {
    // Arrange
    UUID nonExistingRoleId = MockRole.ROLE_ID;
    Role sourceRole = this.role;

    Mockito
      .when(roleRepository.findById(nonExistingRoleId))
      .thenReturn(Optional.empty());
    // Act & Assert
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .patch(basePath + "/{id}", nonExistingRoleId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(sourceRole))
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

    Mockito.verify(roleRepository, Mockito.never()).save(sourceRole);
  }

  @Test
  @DisplayName("Test: Update existing role - Success")
  void testUpdateRoleById_ExistingRole_Success() throws Exception {
    UUID roleId = MockRole.ROLE_ID;
    Role roleToUpdate = this.role;
    roleToUpdate.setId(roleId);
    Mockito
      .when(roleRepository.findById(roleId))
      .thenReturn(Optional.of(roleToUpdate));
    Mockito.when(roleRepository.save(roleToUpdate)).thenReturn(roleToUpdate);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .put(basePath + "/{id}", roleId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(roleToUpdate))
      )
      .andExpect(MockMvcResultMatchers.status().isNoContent())
      .andExpect(MockMvcResultMatchers.content().string("Role PUT success"));

    Mockito.verify(roleRepository, Mockito.times(1)).findById(roleId);
    Mockito.verify(roleRepository, Mockito.times(1)).save(roleToUpdate);
  }

  @Test
  @DisplayName("Test: Update non-existing role - Not Found")
  void testUpdateRoleById_NonExistingRole_NotFound() throws Exception {
    UUID roleId = MockRole.ROLE_ID;
    Role roleToUpdate = this.role;

    Mockito.when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .put(basePath + "/{id}", roleId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(roleToUpdate))
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

    Mockito.verify(roleRepository, Mockito.times(1)).findById(roleId);
    Mockito
      .verify(roleRepository, Mockito.never())
      .save(ArgumentMatchers.any(Role.class));
  }

  @Test
  @DisplayName("Test case Should pass with invalid name")
  void testUpdateRoleById_InvalidInput_EmptyName() throws Exception {
    // Prepare test data with invalid input
    UUID roleId = MockRole.ROLE_ID;
    Role roleToUpdate = this.role;
    roleToUpdate.setName("");

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .put(basePath + "/{id}", roleId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(roleToUpdate))
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(result ->
        Assertions.assertTrue(
          result.getResolvedException() instanceof MethodArgumentNotValidException
        )
      );

    Mockito.verify(roleRepository, Mockito.never()).findById(MockRole.ROLE_ID);
    Mockito
      .verify(roleRepository, Mockito.never())
      .save(ArgumentMatchers.any(Role.class));
  }

  @Test
  @DisplayName("Test case Should pass with invalid RolType")
  void testUpdateRoleById_InvalidInput_EmptyRole() throws Exception {
    // Prepare test data with invalid input

    UUID roleId = MockRole.ROLE_ID;
    Role roleToUpdate = this.role;
    role.setRoleType(null);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .put(basePath + "/{id}", roleId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(roleToUpdate))
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(result ->
        Assertions.assertTrue(
          result.getResolvedException() instanceof MethodArgumentNotValidException
        )
      );
    Mockito.verify(roleRepository, Mockito.never()).findById(MockRole.ROLE_ID);
    Mockito
      .verify(roleRepository, Mockito.never())
      .save(ArgumentMatchers.any(Role.class));
  }

  @Test
  @DisplayName("Test:Delete existing role by Id")
  void testDeleteRoleById_ExistingRole_Success() throws Exception {
    UUID roleId = MockRole.ROLE_ID;

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .delete(basePath + "/{id}", roleId)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isNoContent())
      .andExpect(
        MockMvcResultMatchers.jsonPath("$").value("Roles DELETE success")
      );
    Mockito.verify(roleRepository, Mockito.times(1)).deleteById(roleId);
  }

  @Test
  @DisplayName("Delete non-existing roles should return 404(not found) status")
  void testDeleteRolesById_NotFound() throws Exception {
    UUID existingId = MockRole.ROLE_ID;
    // Mock the behavior of dependencies

    Mockito
      .doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
      .when(roleRepository)
      .deleteById(existingId);

    // Call the method under test
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .delete(basePath + "/{id}", existingId)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isNotFound());

    Mockito.verify(roleRepository).deleteById(existingId);
  }
}
