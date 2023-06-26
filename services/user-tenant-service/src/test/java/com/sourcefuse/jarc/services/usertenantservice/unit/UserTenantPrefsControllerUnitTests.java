package com.sourcefuse.jarc.services.usertenantservice.unit;

import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenant;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenantPrefs;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockCurrentUserSession;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockTenantUser;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserTenantPrefsRepository;
import com.sourcefuse.jarc.services.usertenantservice.service.UserTenantPrefsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("User Tenant Prefs Controller Unit Tests")
public class UserTenantPrefsControllerUnitTests {

  @Mock
  private UserTenantPrefsRepository userTenantPrefsRepository;

  @InjectMocks
  private UserTenantPrefsServiceImpl userTenantPrefsService;

  private UserTenantPrefs userTenantPrefs;
  private UUID mockUserTenantId;

  @BeforeEach
  public void setup() {
    userTenantPrefs = MockTenantUser.geUserTenantPrefsObj();
    mockUserTenantId = MockTenantUser.USER_TENANT_ID;
    userTenantPrefs.setUserTenant(new UserTenant(mockUserTenantId));
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("Create Tenant Prefs - Success")
  public void testCreateTenantPrefs_Success() throws Exception {
    // Set Current User

    MockCurrentUserSession.setCurrentLoggedInUser(null, null, mockUserTenantId);
    // Mock the repository
    when(userTenantPrefsRepository.save(any(UserTenantPrefs.class)))
      .thenReturn(userTenantPrefs);

    // Act and Assert
    userTenantPrefsService.createTenantPrefs(userTenantPrefs);

    // Verify the repository save method was called
    verify(userTenantPrefsRepository, times(1))
      .save(any(UserTenantPrefs.class));
  }

  @Test
  @DisplayName("Create Tenant Prefs - Existing Tenant Prefs records Updated")
  public void testCreateTenantPrefs_ExistingPrefsUpdated() throws Exception {
    // Arrange
    MockCurrentUserSession.setCurrentLoggedInUser(null, null, mockUserTenantId);

    // Set up preExistsTenantPrefs object
    UserTenantPrefs preExistsTenantPrefs = new UserTenantPrefs();

    // Mock the repository
    when(userTenantPrefsRepository.findOne(any(Specification.class)))
      .thenReturn(Optional.of(preExistsTenantPrefs));
    when(userTenantPrefsRepository.save(any(UserTenantPrefs.class)))
      .thenReturn(preExistsTenantPrefs);

    // Act and Assert
    userTenantPrefsService.createTenantPrefs(userTenantPrefs);

    // Verify the repository getByUserTenantIdAndConfigKey and save methods were called
    verify(userTenantPrefsRepository, times(1))
      .findOne(any(Specification.class));
    verify(userTenantPrefsRepository, times(1))
      .save(any(UserTenantPrefs.class));
  }
}
