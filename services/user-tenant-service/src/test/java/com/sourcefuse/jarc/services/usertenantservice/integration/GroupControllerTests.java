package com.sourcefuse.jarc.services.usertenantservice.integration;

import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcefuse.jarc.core.constants.CommonConstants;
import com.sourcefuse.jarc.services.usertenantservice.controller.GroupController;
import com.sourcefuse.jarc.services.usertenantservice.dto.Group;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserGroup;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockCurrentUserSession;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockGroup;
import com.sourcefuse.jarc.services.usertenantservice.repository.GroupRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserGroupsRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.springframework.web.server.ResponseStatusException;

@DisplayName("Create Group  Apis Integration/units Tests")
@ExtendWith(MockitoExtension.class)
public class GroupControllerTests {

  @Mock
  private GroupRepository groupRepository;

  @Mock
  private UserGroupsRepository userGroupsRepository;

  @InjectMocks
  private GroupController groupController;

  private Group group;
  private UserGroup userGroup;
  private UUID mockGroupId;
  private MockMvc mockMvc;
  private String basePath = "/groups";

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
    //prepare the test data
    userGroup = new UserGroup();

    group = MockGroup.getGroupObj();
    mockGroupId = MockGroup.GROUP_ID;
  }

  @Test
  @DisplayName("Create Groups - Success")
  public void testCreateGroups_Success() throws Exception {
    // Mock the necessary objects and methods
    Group savedGroup = new Group();
    savedGroup.setId(MockGroup.GROUP_ID);
    savedGroup.setName("Test");
    MockCurrentUserSession.setCurrentLoggedInUser(null, null, null);

    Mockito.when(groupRepository.save(any(Group.class))).thenReturn(savedGroup);
    Mockito
      .when(userGroupsRepository.save(any(UserGroup.class)))
      .thenReturn(userGroup);

    // Call the method under test
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .content(asJsonString(group))
      )
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(
        MockMvcResultMatchers
          .jsonPath("$.id")
          .value(savedGroup.getId().toString())
      )
      .andExpect(
        MockMvcResultMatchers.jsonPath("$.name").value(savedGroup.getName())
      );

    // Verify the expected interactions and assertions
    Mockito.verify(groupRepository, Mockito.times(1)).save(any(Group.class));
    Mockito
      .verify(userGroupsRepository, Mockito.times(1))
      .save(any(UserGroup.class));
  }

  @Test
  @DisplayName("Test case Should pass for database error")
  void testCreateGroups_DatabaseError() throws Exception {
    // Mock a database error when saving the group
    Mockito
      .when(groupRepository.save(any(Group.class)))
      .thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath)
          .contentType(MediaType.APPLICATION_JSON)
          .content(asJsonString(group))
      )
      .andExpect(MockMvcResultMatchers.status().isInternalServerError())
      .andExpect(result ->
        Assertions.assertTrue(
          result.getResolvedException() instanceof ResponseStatusException
        )
      )
      .andExpect(result ->
        Assertions.assertEquals(
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          result.getResponse().getStatus()
        )
      );

    // Verify that the save method was called
    Mockito.verify(groupRepository).save(any(Group.class));
    // Verify that the save method was never called
    Mockito
      .verify(userGroupsRepository, Mockito.never())
      .save(any(UserGroup.class));
  }

  @Test
  @DisplayName("Test case Should pass for database error")
  void testCreate_UserGroupsRepo_DatabaseError() throws Exception {
    // Mock a database error when saving the group
    Group savedGroup = new Group();

    MockCurrentUserSession.setCurrentLoggedInUser(null, null, null);

    Mockito.when(groupRepository.save(any(Group.class))).thenReturn(savedGroup);
    Mockito
      .when(userGroupsRepository.save(any(UserGroup.class)))
      .thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath)
          .contentType(MediaType.APPLICATION_JSON)
          .content(asJsonString(group))
      )
      .andExpect(MockMvcResultMatchers.status().isInternalServerError())
      .andExpect(result ->
        Assertions.assertTrue(
          result.getResolvedException() instanceof ResponseStatusException
        )
      )
      .andExpect(result ->
        Assertions.assertEquals(
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          result.getResponse().getStatus()
        )
      );
    // Verify that the save method was called
    Mockito.verify(groupRepository).save(any(Group.class));
    // Verify that the save method was never called
    Mockito.verify(userGroupsRepository).save(any(UserGroup.class));
  }

  @Test
  @DisplayName("Test: case for count success")
  public void testCount_Success() throws Exception {
    // Mock the behavior of roleRepository.count()
    Mockito.when(groupRepository.count()).thenReturn(5L); // Mock a count of 5

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
    Mockito.verify(groupRepository).count();
  }

  @Test
  @DisplayName("Test case should pass for 0 count")
  public void testCount_Empty() throws Exception {
    // Mock the behavior of roleRepository.count()
    Mockito.when(groupRepository.count()).thenReturn(0L); // Mock a count of 0

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
    Mockito.verify(groupRepository).count();
  }

  @Test
  @DisplayName("Test: countGroup with database error")
  public void testCountGroup_DatabaseError() throws Exception {
    // Mock the behavior of roleRepository.save() to throw an exception
    Mockito
      .when(groupRepository.count())
      .thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

    // Perform the API call and catch the exception
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get(basePath + "/count")
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isInternalServerError())
      .andExpect(result ->
        Assertions.assertTrue(
          result.getResolvedException() instanceof ResponseStatusException
        )
      )
      .andExpect(result ->
        Assertions.assertEquals(
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          result.getResponse().getStatus()
        )
      );

    // Verify that roleRepository.save() was called
    Mockito.verify(groupRepository).count();
  }

  @Test
  @DisplayName("Test getAllGroups success")
  public void testGetAllGroups_Success() throws Exception {
    // Prepare test data
    List<Group> groups = Arrays.asList(
      new Group(UUID.randomUUID()),
      new Group(UUID.randomUUID()),
      new Group(UUID.randomUUID())
    );

    // Mock the behavior of roleRepository.findAll()
    Mockito.when(groupRepository.findAll()).thenReturn(groups);

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
    Mockito.verify(groupRepository).findAll();
  }

  @Test
  @DisplayName("Test: getAllGroups Empty Response")
  public void testGetAllGroup_Empty() throws Exception {
    // Mock the behavior of roleRepository.findAll()
    Mockito.when(groupRepository.findAll()).thenReturn(Arrays.asList());

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
    Mockito.verify(groupRepository).findAll();
  }

  @Test
  @DisplayName("Test: GetGroupByID with existing group")
  public void testGetGroupByID_ExistingRole() throws Exception {
    //get Current User
    MockCurrentUserSession.setCurrentLoggedInUser(null, null, null);
    // Mock the behavior of roleRepository.findById()
    Mockito
      .when(groupRepository.findOne(any(Specification.class)))
      .thenReturn(Optional.of(group));

    // Perform the API call
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get(basePath + "/{id}", mockGroupId)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(
        MockMvcResultMatchers.jsonPath("$.id").value(group.getId().toString())
      )
      .andExpect(
        MockMvcResultMatchers.jsonPath("$.name").value(group.getName())
      );

    // Verify that roleRepository.findOne() was called
    Mockito.verify(groupRepository).findOne(any(Specification.class));
  }

  @Test
  @DisplayName("Test: getGroupByID with non-existing group")
  public void testGetGroupByID_NonExistingRole() throws Exception {
    //get Current User
    MockCurrentUserSession.setCurrentLoggedInUser(null, null, null);
    // Mock the behavior of roleRepository.findById()
    Mockito
      .when(groupRepository.findOne(any(Specification.class)))
      .thenReturn(Optional.empty());

    // Perform the API call and catch the exception

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get(basePath + "/{id}", mockGroupId)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isNotFound())
      .andExpect(result ->
        Assertions.assertTrue(
          result.getResolvedException() instanceof ResponseStatusException
        )
      )
      .andExpect(result ->
        Assertions.assertEquals(
          HttpStatus.NOT_FOUND.value(),
          result.getResponse().getStatus()
        )
      )
      .andExpect(result ->
        Assertions.assertEquals(
          CommonConstants.NO_GRP_PRESENT,
          result.getResponse().getErrorMessage()
        )
      );

    // Verify that roleRepository.findById() was called
    Mockito.verify(groupRepository).findOne(any(Specification.class));
  }

  @Test
  @DisplayName("Test Update Group - Existing Group - Success")
  void testUpdateGroup_ExistingGroup_Success() throws Exception {
    // Arrange
    UUID existingGroupId = MockGroup.GROUP_ID;
    Group existingGroup = new Group(existingGroupId);
    Group sourceGroup = this.group;
    //get Current User
    MockCurrentUserSession.setCurrentLoggedInUser(null, null, null);

    Mockito
      .when(groupRepository.findOne(any(Specification.class)))
      .thenReturn(Optional.of(existingGroup));
    Mockito.when(groupRepository.save(existingGroup)).thenReturn(existingGroup);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .patch(basePath + "/{id}", existingGroupId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(asJsonString(sourceGroup))
      )
      .andExpect(MockMvcResultMatchers.status().isNoContent())
      .andExpect(
        MockMvcResultMatchers.jsonPath("$").value("Group PATCH success")
      );

    // Additional assertions
    Mockito
      .verify(groupRepository, Mockito.times(1))
      .findOne(any(Specification.class));
    Mockito.verify(groupRepository, Mockito.times(1)).save(existingGroup);
  }

  @Test
  @DisplayName("Test Update Group - Non-Existing Group - Not Found")
  void testUpdateRole_NonExistingGroup_NotFound() throws Exception {
    // Arrange
    UUID nonExistingGroupId = mockGroupId;
    Group sourceGroup = this.group;

    //get Current User
    MockCurrentUserSession.setCurrentLoggedInUser(null, null, null);
    Mockito
      .when(groupRepository.findOne(any(Specification.class)))
      .thenReturn(Optional.empty());

    // Act & Assert
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .patch(basePath + "/{id}", mockGroupId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(asJsonString(sourceGroup))
      )
      .andExpect(MockMvcResultMatchers.status().isNotFound())
      .andExpect(result ->
        Assertions.assertTrue(
          result.getResolvedException() instanceof ResponseStatusException
        )
      )
      .andExpect(result ->
        Assertions.assertEquals(
          HttpStatus.NOT_FOUND.value(),
          result.getResponse().getStatus()
        )
      )
      .andExpect(result ->
        Assertions.assertEquals(
          CommonConstants.NO_GRP_PRESENT,
          result.getResponse().getErrorMessage()
        )
      );

    Mockito.verify(groupRepository, Mockito.never()).save(sourceGroup);
  }

  @Test
  @DisplayName("Delete existing roles successfully")
  void testDeleteRolesById_Success() throws Exception {
    //get Authenticated User
    MockCurrentUserSession.setCurrentLoggedInUser(null, null, null);
    // Mock the behavior of dependencies
    Mockito
      .when(userGroupsRepository.delete(any(Specification.class)))
      .thenReturn(1L);
    Mockito
      .when(groupRepository.delete(any(Specification.class)))
      .thenReturn(1L);

    // Call the method under test
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .delete(basePath + "/{id}", mockGroupId)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isNoContent())
      .andExpect(
        MockMvcResultMatchers.jsonPath("$").value("Groups DELETE success")
      );

    // Verify the method calls
    Mockito.verify(userGroupsRepository).delete(any(Specification.class));
    Mockito.verify(groupRepository).delete(any(Specification.class));
  }

  @Test
  @DisplayName("Delete non-existing roles should return 404 status")
  void testDeleteRolesById_NotFound() throws Exception {
    Mockito
      .doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
      .when(groupRepository)
      .delete(any(Specification.class));

    // Call the method under test
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .delete(basePath + "/{id}", mockGroupId)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isNotFound());

    // Verify the method calls
    Mockito.verify(userGroupsRepository).delete(any(Specification.class));
    Mockito.verify(groupRepository).delete(any(Specification.class));
  }

  private static String asJsonString(Object obj) throws Exception {
    return new ObjectMapper().writeValueAsString(obj);
  }
}
