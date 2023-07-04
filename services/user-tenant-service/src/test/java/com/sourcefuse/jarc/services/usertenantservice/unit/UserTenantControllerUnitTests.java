package com.sourcefuse.jarc.services.usertenantservice.unit;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenant;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockCurrentUserSession;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockTenantUser;
import com.sourcefuse.jarc.services.usertenantservice.repository.RoleUserTenantRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserViewRepository;
import com.sourcefuse.jarc.services.usertenantservice.service.UserTenantServiceImpl;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.server.ResponseStatusException;

@DisplayName("Create User Tenant Controller Apis unit Tests")
class UserTenantControllerUnitTests {

  @Mock
  private RoleUserTenantRepository roleUserTenantRepository;

  @Mock
  private UserViewRepository userViewRepository;

  @InjectMocks
  private UserTenantServiceImpl userTenantService;

  private UserTenant userTenant;

  private UUID mockUserTenantId;

  @BeforeEach
  public void setup() {
    //prepare the test data
    MockitoAnnotations.openMocks(this);
    userTenant = MockTenantUser.getUserTenantObj();
    mockUserTenantId = MockTenantUser.USER_TENANT_ID;
    //Set Current LoggedIn User
    MockCurrentUserSession.setCurrentLoggedInUser(null, null, null);
  }

  @Test
  @DisplayName("Get User Tenant by ID - Success")
  void testGetUserTenantByIdSuccess() throws Exception {
    MockCurrentUserSession.setCurrentLoggedInUser(
      userTenant.getTenant().getId(),
      userTenant.getUser().getId(),
      null
    );
    // Mock the repository
    when(roleUserTenantRepository.findById(mockUserTenantId))
      .thenReturn(Optional.of(userTenant));

    UserView userView = new UserView();
    userView.setUserTenantId(mockUserTenantId);
    when(userViewRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.ofNullable(userView));

    // Act and Assert
    userTenantService.getUserTenantById(mockUserTenantId);

    //verify method calls
    verify(userViewRepository, times(1))
      .findOne(Mockito.any(Specification.class));
  }

  @Test
  @DisplayName("Get User Tenant by ID - Forbidden")
  void testGetUserTenantByIdForbidden() throws Exception {
    MockCurrentUserSession.setCurrentLoggedInUser(
      MockTenantUser.INVALID_ID,
      userTenant.getUser().getId(),
      null
    );
    // Mock the repository
    when(roleUserTenantRepository.findById(mockUserTenantId))
      .thenReturn(Optional.of(userTenant));

    // Act and Assert
    Assertions.assertThrows(
      ResponseStatusException.class,
      () -> userTenantService.getUserTenantById(mockUserTenantId)
    );

    verify(userViewRepository, never())
      .findOne(Mockito.any(Specification.class));
  }

  @Test
  @DisplayName("Get User Tenant by ID - User Not Found")
  void testGetUserTenantByIdNotFound() throws Exception {
    MockCurrentUserSession.setCurrentLoggedInUser(
      userTenant.getTenant().getId(),
      userTenant.getUser().getId(),
      null
    );
    // Mock the repository
    when(roleUserTenantRepository.findById(mockUserTenantId))
      .thenReturn(Optional.empty());
    // Act and Assert
    Assertions.assertThrows(
      ResponseStatusException.class,
      () -> userTenantService.getUserTenantById(mockUserTenantId)
    );

    verify(userViewRepository, never())
      .findOne(Mockito.any(Specification.class));
  }
}
