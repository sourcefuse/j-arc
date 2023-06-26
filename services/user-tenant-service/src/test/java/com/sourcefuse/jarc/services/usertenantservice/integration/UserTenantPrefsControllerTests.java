package com.sourcefuse.jarc.services.usertenantservice.integration;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcefuse.jarc.services.usertenantservice.controller.UserTenantPrefsController;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenantPrefs;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockCurrentUserSession;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockTenantUser;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserTenantPrefsRepository;
import com.sourcefuse.jarc.services.usertenantservice.service.UserTenantPrefsService;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@DisplayName("User Tenant Prefs Controller Integration Tests")
@ExtendWith(MockitoExtension.class)
public class UserTenantPrefsControllerTests {

  private MockMvc mockMvc;

  @Mock
  private UserTenantPrefsRepository userTenantPrefsRepository;

  @Mock
  private UserTenantPrefsService userTenantPrefsService;

  @InjectMocks
  private UserTenantPrefsController userTenantPrefsController;

  private UserTenantPrefs userTenantPrefs;
  private UUID userTenantId;

  private String basePath = "/user-tenant-prefs";

  @BeforeEach
  public void setup() {
    this.mockMvc =
      MockMvcBuilders.standaloneSetup(userTenantPrefsController).build();
    userTenantPrefs = MockTenantUser.geUserTenantPrefsObj();
  }

  @Test
  @DisplayName("Create Tenant Prefs - Success")
  public void testCreateTenantPrefs_Success() throws Exception {
    // Set Current User
    MockCurrentUserSession.setCurrentLoggedInUser(null, null, userTenantId);

    // Mock the repository
    when(userTenantPrefsService.createTenantPrefs(any(UserTenantPrefs.class)))
      .thenReturn(userTenantPrefs);

    // Act and Assert
    mockMvc
      .perform(
        post(basePath)
          .contentType(MediaType.APPLICATION_JSON)
          .content(asJsonString(userTenantPrefs))
      )
      .andExpect(status().isCreated())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(
        jsonPath("$.configKey").value(userTenantPrefs.getConfigKey().toString())
      )
      .andExpect(
        jsonPath("$.configValue").value(userTenantPrefs.getConfigValue())
      );

    // Verify the repository save method was called
    verify(userTenantPrefsService, times(1))
      .createTenantPrefs(any(UserTenantPrefs.class));
  }

  @Test
  @DisplayName("Get All User Tenant Preferences - Success")
  public void testGetAllUsTenantPrefs() throws Exception {
    // Arrange
    UserTenantPrefs userTenantPrefs1 = UserTenantPrefs
      .builder()
      .id(UUID.randomUUID())
      .build();
    UserTenantPrefs userTenantPrefs2 = UserTenantPrefs
      .builder()
      .id(UUID.randomUUID())
      .build();
    List<UserTenantPrefs> userTenantPrefsList = Arrays.asList(
      userTenantPrefs1,
      userTenantPrefs2
    );

    // Mock the repository
    Mockito
      .when(userTenantPrefsRepository.findAll())
      .thenReturn(userTenantPrefsList);

    // Set up the mockMvc with the controller
    mockMvc =
      MockMvcBuilders.standaloneSetup(userTenantPrefsController).build();

    // Act and Assert
    mockMvc
      .perform(
        get("/user-tenant-prefs").contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()").value(2))
      .andExpect(jsonPath("$[0].id").exists())
      .andExpect(jsonPath("$[1].id").exists());
  }

  @Test
  @DisplayName("Get All User Tenant Preferences - Empty Response")
  public void testGetAllUsTenantPrefs_Empty() throws Exception {
    // Arrange
    List<UserTenantPrefs> userTenantPrefsList = new ArrayList<>();

    // Mock the repository
    Mockito
      .when(userTenantPrefsRepository.findAll())
      .thenReturn(userTenantPrefsList);

    // Set up the mockMvc with the controller
    mockMvc =
      MockMvcBuilders.standaloneSetup(userTenantPrefsController).build();

    // Act and Assert
    mockMvc
      .perform(
        get("/user-tenant-prefs").contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()").value(0));
  }

  private static String asJsonString(Object obj) throws Exception {
    return new ObjectMapper().writeValueAsString(obj);
  }
}
