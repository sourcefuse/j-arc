package com.sourcefuse.jarc.services.usertenantservice.integration;

import com.sourcefuse.jarc.core.filters.services.QueryService;
import com.sourcefuse.jarc.services.usertenantservice.controller.UserGroupController;
import com.sourcefuse.jarc.services.usertenantservice.dto.Group;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserGroup;
import com.sourcefuse.jarc.services.usertenantservice.mocks.JsonUtils;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockCurrentUserSession;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockGroup;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockSpecification;
import com.sourcefuse.jarc.services.usertenantservice.repository.GroupRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserGroupsRepository;
import com.sourcefuse.jarc.services.usertenantservice.service.UserGroupServiceImpl;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Create Role User Tenants Apis Integration Tests")
@ExtendWith(MockitoExtension.class)
class UserGroupControllerTests {

  @Mock
  GroupRepository groupRepository;

  @Mock
  UserGroupsRepository userGroupsRepo;

  @Mock
  UserGroupServiceImpl userGroupService;

  @InjectMocks
  UserGroupController userGroupController;

  private UUID mockGroupId;

  private Group group;

  private UserGroup userGroup;

  private MockMvc mockMvc;

  private String basePath = "/groups/{id}/user-groups/";

  @Mock
  private QueryService queryService;

  @BeforeEach
  public void setup() {
    //prepare the test data
    userGroup = MockGroup.getUserGroupObj();

    group = MockGroup.getGroupObj();
    group.setTenantId(userGroup.getTenantId());

    mockGroupId = MockGroup.GROUP_ID;
    mockMvc = MockMvcBuilders.standaloneSetup(userGroupController).build();
    //Set Current LoggedIn User
    MockCurrentUserSession.setCurrentLoggedInUser(null, null, null);
  }

  @Test
  @DisplayName("Create User Group - Success")
  void testCreateUserGroup_Success() throws Exception {
    // Mocked role
    Group mockRole = this.group;

    // Mocking repository behavior
    when(userGroupService.createUserGroup(userGroup, mockGroupId))
      .thenReturn(userGroup);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath, mockGroupId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(userGroup))
      )
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(
        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(
        MockMvcResultMatchers
          .jsonPath("$.id")
          .value(userGroup.getId().toString())
      );

    // Verify repository method invocations
    verify(userGroupService, (times(1)))
      .createUserGroup(userGroup, mockGroupId);
  }

  @Test
  @DisplayName("Test: Should pass with invalid userTenantId")
  void testUserGrp_Invalid_userTenantId() throws Exception {
    // Prepare test data with invalid input;
    userGroup.setUsrTnt(null);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath, mockGroupId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(userGroup))
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(result ->
        Assertions.assertTrue(
          result.getResolvedException() instanceof MethodArgumentNotValidException
        )
      );

    verify(userGroupService, (never())).createUserGroup(userGroup, mockGroupId);
  }

  @Test
  @DisplayName("Test: Should pass with invalid Group ID ")
  void testUserGrp_Invalid_GroupID() throws Exception {
    // Prepare test data with invalid input;
    userGroup.setGrp(null);
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath, mockGroupId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(userGroup))
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(result ->
        Assertions.assertTrue(
          result.getResolvedException() instanceof MethodArgumentNotValidException
        )
      );

    verify(userGroupService, (never())).createUserGroup(userGroup, mockGroupId);
  }

  @Test
  @DisplayName("Update User Group - Success Integration")
  void testUpdateUserGroup_Success() throws Exception {
    Mockito
      .doNothing()
      .when(userGroupService)
      .updateAllUserGroup(mockGroupId, userGroup, userGroup.getId());

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .patch(basePath + "/{userGroupId}", mockGroupId, userGroup.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(userGroup))
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(
        MockMvcResultMatchers
          .jsonPath("$")
          .value("Group.UserGroup PATCH success count")
      );

    // Verify the calls to the repositories
    verify(userGroupService, times(1))
      .updateAllUserGroup(mockGroupId, userGroup, userGroup.getId());
  }

  @Test
  @DisplayName("Delete User Group - Success - Integration")
  void testDeleteUsrGrp_Success() throws Exception {
    // Mock current user and authentication

    // Mock userGroupsRepo methods
    Mockito
      .doNothing()
      .when(userGroupService)
      .deleteUserGroup(group.getId(), userGroup.getId());

    mockMvc
      .perform(
        MockMvcRequestBuilders.delete(
          basePath + userGroup.getId(),
          group.getId(),
          userGroup.getId()
        )
      )
      .andExpect(MockMvcResultMatchers.status().isOk());

    // Verify that the deleteUserGroup was called
    verify(userGroupService, times(1))
      .deleteUserGroup(group.getId(), userGroup.getId());
  }

  @Test
  @DisplayName("Test Get All User Groups By GroupId")
  void testGetAllUserGroupsByRole() throws Exception {
    List<UserGroup> userGroupList = new ArrayList<>();
    userGroupList.add(new UserGroup());
    userGroupList.add(new UserGroup());

    Specification mockSpecification = MockSpecification.getSpecification(
      queryService
    );
    when(groupRepository.findOne(ArgumentMatchers.any(Specification.class)))
      .thenReturn(Optional.of(group));
    when(userGroupsRepo.findAll(mockSpecification)).thenReturn(userGroupList);

    mockMvc
      .perform(MockMvcRequestBuilders.get(basePath, mockGroupId))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
      .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());

    verify(groupRepository, times(1))
      .findOne(ArgumentMatchers.any(Specification.class));
    verify(userGroupsRepo, times(1)).findAll(mockSpecification);
  }

  @Test
  @DisplayName("Test Get All User Group By Role - Group Not Found")
  void testGetAllUserGroupsByRole_GroupNotFound() throws Exception {
    Mockito
      .when(groupRepository.findOne(ArgumentMatchers.any(Specification.class)))
      .thenReturn(Optional.empty());

    mockMvc
      .perform(MockMvcRequestBuilders.get(basePath, mockGroupId))
      .andExpect(MockMvcResultMatchers.status().isNotFound())
      .andExpect(
        MockMvcResultMatchers
          .status()
          .reason("No group is present against given value")
      );

    Mockito
      .verify(groupRepository, Mockito.times(1))
      .findOne(ArgumentMatchers.any(Specification.class));
    Mockito
      .verify(userGroupsRepo, Mockito.never())
      .findAll(ArgumentMatchers.any(Specification.class));
  }

  @Test
  @DisplayName("Test countUserGroup - Success")
  void testCountUserGroup_Success() throws Exception {
    long expectedCount = 10;

    when(userGroupsRepo.count(ArgumentMatchers.any(Specification.class)))
      .thenReturn(expectedCount);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get(basePath + "/count", mockGroupId)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(
        MockMvcResultMatchers.jsonPath("$.count").value(expectedCount)
      );
  }

  @Test
  @DisplayName("Test countUserGroup - Group Not Found")
  void testCountUserGroup_GroupNotFound() throws Exception {
    doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
      .when(userGroupsRepo)
      .count(ArgumentMatchers.any(Specification.class));

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get(basePath + "/count", mockGroupId)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isNotFound());
  }
}
