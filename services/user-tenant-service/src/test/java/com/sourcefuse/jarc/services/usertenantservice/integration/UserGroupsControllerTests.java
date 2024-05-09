package com.sourcefuse.jarc.services.usertenantservice.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sourcefuse.jarc.core.filters.services.QueryService;
import com.sourcefuse.jarc.services.usertenantservice.controller.UserGroupsController;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserGroup;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockCurrentUserSession;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockGroup;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserGroupsRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@DisplayName("User Groups Apis Integration /unit Tests")
@ExtendWith(MockitoExtension.class)
class UserGroupsControllerTests {

  private MockMvc mockMvc;

  @InjectMocks
  private UserGroupsController userGroupsController;

  @Mock
  UserGroupsRepository userGroupsRepository;

  private String basePath = "/user-groups";

  @Mock
  private QueryService queryService;

  private Specification mockSpecification;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(userGroupsController).build();
    //Set Current LoggedIn User
    MockCurrentUserSession.setCurrentLoggedInUser(null, null, null);
    mockSpecification = null;
  }

  @Test
  @DisplayName("Fetch All User Groups - Success")
  void testFetchAllUserGroups() throws Exception {
    // Arrange
    UUID groupId = MockGroup.GROUP_ID;
    UserGroup userGroup1 = UserGroup.builder().id(groupId).build();

    UUID groupId2 = MockGroup.GROUP_ID_TWO;
    UserGroup userGroup2 = UserGroup.builder().id(groupId2).build();

    List<UserGroup> userGroupsList = Arrays.asList(userGroup1, userGroup2);

    // Mock the repository
    Mockito
      .when(userGroupsRepository.findAll(mockSpecification))
      .thenReturn(userGroupsList);

    // Act and Assert
    mockMvc
      .perform(get(basePath).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()").value(2))
      .andExpect(jsonPath("$[0].id").value(String.valueOf(groupId)))
      .andExpect(jsonPath("$[1].id").value(String.valueOf(groupId2)));
  }

  @Test
  @DisplayName("Fetch All User Groups - Empty Response ")
  void testFetchAllUserGroups_Empty() throws Exception {
    // Arrange

    List<UserGroup> userGroupsList = new ArrayList<>();

    // Mock the repository
    Mockito
      .when(userGroupsRepository.findAll(mockSpecification))
      .thenReturn(userGroupsList);

    // Act and Assert
    mockMvc
      .perform(get(basePath).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.size()").value(0));
  }

  @Test
  @DisplayName("Count User Groups - Success")
  void testCountUserGroups() throws Exception {
    // Arrange
    UserGroup userGroup1 = UserGroup.builder().build();
    UserGroup userGroup2 = UserGroup.builder().build();

    List<UserGroup> userGroupsList = Arrays.asList(userGroup1, userGroup2);

    // Mock the repository
    Mockito
      .when(userGroupsRepository.findAll(mockSpecification))
      .thenReturn(userGroupsList);

    // Act and Assert
    mockMvc
      .perform(get(basePath + "/count").contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.count").value(2));
  }

  @Test
  @DisplayName("Count User Groups - Zero Count -No Groups Present")
  void testCountUserGroups_Empty() throws Exception {
    List<UserGroup> userGroupsList = new ArrayList<>();

    // Mock the repository
    Mockito
      .when(userGroupsRepository.findAll(mockSpecification))
      .thenReturn(userGroupsList);

    // Act and Assert
    mockMvc
      .perform(get(basePath + "/count").contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.count").value(0));
  }
}
