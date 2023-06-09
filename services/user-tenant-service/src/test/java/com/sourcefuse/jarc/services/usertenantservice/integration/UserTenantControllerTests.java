package com.sourcefuse.jarc.services.usertenantservice.integration;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sourcefuse.jarc.services.usertenantservice.controller.UserTenantController;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockCurrentUserSession;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockTenantUser;
import com.sourcefuse.jarc.services.usertenantservice.service.UserTenantServiceImpl;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriTemplate;

@DisplayName("Create UserTenant Apis Integration/units Tests")
@ExtendWith(MockitoExtension.class)
class UserTenantControllerTests {

  @Mock
  private UserTenantServiceImpl userTenantService;

  @InjectMocks
  private UserTenantController userTenantController;

  private MockMvc mockMvc;
  private UUID userTenantId;

  @BeforeEach
  public void setUp() {
    // Set up the mockMvc with the controller
    mockMvc = MockMvcBuilders.standaloneSetup(userTenantController).build();
    userTenantId = MockTenantUser.USER_TENANT_ID;
    //Set Current LoggedIn User
    MockCurrentUserSession.setCurrentLoggedInUser(null, null, null);
  }

  @Test
  @DisplayName("Get User Tenant by ID - Success - Integration")
  void testGetUserTenantByIdSuccess() throws Exception {
    // Mock the repository
    when(userTenantService.getUserTenantById(userTenantId))
      .thenReturn(new UserView());

    // Act and Assert
    mockMvc
      .perform(
        get(new UriTemplate("/user-tenants/{id}").expand(userTenantId))
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk());

    //verify method calls
    verify(userTenantService, times(1)).getUserTenantById(userTenantId);
  }
}
