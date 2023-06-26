package com.sourcefuse.jarc.services.usertenantservice.unit;

import com.sourcefuse.jarc.services.usertenantservice.dto.Tenant;
import com.sourcefuse.jarc.services.usertenantservice.dto.TenantConfig;
import com.sourcefuse.jarc.services.usertenantservice.enums.AuthorizeErrorKeys;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockCurrentUserSession;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockTenantUser;
import com.sourcefuse.jarc.services.usertenantservice.repository.TenantConfigRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.TenantRepository;
import com.sourcefuse.jarc.services.usertenantservice.service.TenantServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@DisplayName("Create Tenant  Apis units Tests")
 class TenantControllerUnitTests {

  @InjectMocks
  private TenantServiceImpl tenantService;

  @Mock
  private TenantRepository tenantRepository;

  @Mock
  private TenantConfigRepository tenantConfigRepository;

  private UUID mockTenantID;
  private Tenant tenant;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    mockTenantID = MockTenantUser.TENANT_ID;
    tenant = MockTenantUser.geTenantObj();
    tenant.setId(mockTenantID);
  }

  @Test
  @DisplayName("Should return tenant when it exists")
  void testFindTenantById_Exists() {
    // Arrange
    Tenant expectedTenant = new Tenant(mockTenantID);

    MockCurrentUserSession.setCurrentLoggedInUser(mockTenantID, null, null);

    Mockito
      .when(tenantRepository.findById(mockTenantID))
      .thenReturn(Optional.of(expectedTenant));

    // Act
    Tenant result = tenantService.fetchTenantByID(mockTenantID);

    // Assert
    Assertions.assertEquals(expectedTenant, result);
    Mockito.verify(tenantRepository, Mockito.times(1)).findById(mockTenantID);
  }

  @Test
  @DisplayName("Should throw exception when tenant does not exist")
  void testFindTenantById_NotExists() {
    // Arrange

    MockCurrentUserSession.setCurrentLoggedInUser(mockTenantID, null, null);
    Mockito
      .when(tenantRepository.findById(mockTenantID))
      .thenReturn(Optional.empty());

    // Act and Assert
    Assertions.assertThrows(
      ResponseStatusException.class,
      () -> tenantService.fetchTenantByID(mockTenantID)
    );
    Mockito.verify(tenantRepository, Mockito.times(1)).findById(mockTenantID);
  }

  @Test
  @DisplayName(
    "Should throw exception when user does not have access permission"
  )
  void testFindTenantById_NoAccessPermission() {
    // Arrange
    MockCurrentUserSession.setCurrentLoggedInUser(
      MockTenantUser.INVALID_ID,
      null,
      null
    ); // Different tenant ID than the one requested

    // Act and Assert
    ResponseStatusException exception = Assertions.assertThrows(
      ResponseStatusException.class,
      () -> tenantService.fetchTenantByID(mockTenantID)
    );
    Assertions.assertEquals(
      AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString(),
      exception.getReason()
    );
  }

  @Test
  @DisplayName("Update Tenant By ID - Success")
   void testUpdateTenantsById_Success() {
    Tenant sourceTenant = this.tenant;

    Tenant targetTenant = new Tenant();
    targetTenant.setId(mockTenantID);
    targetTenant.setName("Original Tenant");
    //get Current User
    MockCurrentUserSession.setCurrentLoggedInUser(mockTenantID, null, null);

    Mockito
      .when(tenantRepository.findById(mockTenantID))
      .thenReturn(Optional.of(targetTenant));
    Mockito
      .when(tenantRepository.save(any(Tenant.class)))
      .thenReturn(targetTenant);

    Assertions.assertDoesNotThrow(() ->
      tenantService.updateTenantsById(sourceTenant, mockTenantID)
    );
    Assertions.assertEquals(sourceTenant.getName(), targetTenant.getName());

    Mockito.verify(tenantRepository, Mockito.times(1)).findById(mockTenantID);
    Mockito.verify(tenantRepository, Mockito.times(1)).save(targetTenant);
  }

  @Test
  @DisplayName("Update Tenant By ID - Tenant Not Found")
   void testUpdateTenantsById_TenantNotFound() {
    Tenant sourceTenant = this.tenant;
    sourceTenant.setName("Updated Tenant");
    MockCurrentUserSession.setCurrentLoggedInUser(mockTenantID, null, null);
    Mockito
      .when(tenantRepository.findById(mockTenantID))
      .thenReturn(Optional.empty());

    ResponseStatusException exception = Assertions.assertThrows(
      ResponseStatusException.class,
      () -> tenantService.updateTenantsById(sourceTenant, mockTenantID)
    );

    Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

    Mockito.verify(tenantRepository, Mockito.times(1)).findById(mockTenantID);
    Mockito.verify(tenantRepository, Mockito.never()).save(any(Tenant.class));
  }

  @Test
  @DisplayName("Update Tenant By ID - Access Forbidden")
   void testUpdateTenantsById_AccessForbidden() {
    Tenant sourceTenant = new Tenant();
    sourceTenant.setName("Updated Tenant");

    MockCurrentUserSession.setCurrentLoggedInUser(
      MockTenantUser.INVALID_ID,
      null,
      null
    );
    ResponseStatusException exception = Assertions.assertThrows(
      ResponseStatusException.class,
      () -> tenantService.updateTenantsById(sourceTenant, mockTenantID)
    );

    Assertions.assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());

    Mockito.verify(tenantRepository, Mockito.never()).findById(any(UUID.class));
    Mockito.verify(tenantRepository, Mockito.never()).save(any(Tenant.class));
  }

  @Test
  @DisplayName("Delete Tenant by ID - Successful")
   void testDeleteTenantById_Success() {
    // Mocking the checkViewDeleteTenantAccessPermission method
    MockCurrentUserSession.setCurrentLoggedInUser(mockTenantID, null, null);

    // Mocking the tenantRepository.deleteById method
    Mockito.doNothing().when(tenantRepository).deleteById(mockTenantID);

    // Calling the deleteById method
    tenantService.deleteById(mockTenantID);

    // Verifying that the deleteById method is called
    Mockito.verify(tenantRepository).deleteById(mockTenantID);
  }

  @Test
  @DisplayName("Delete Tenant by ID - Forbidden")
   void testDeleteTenantById_Forbidden() {
    UUID tenantId = MockTenantUser.TENANT_ID;

    // Mocking the checkViewDeleteTenantAccessPermission method
    MockCurrentUserSession.setCurrentLoggedInUser(
      MockTenantUser.INVALID_ID,
      null,
      null
    );

    // Calling the deleteById method and expecting a ResponseStatusException
    ResponseStatusException exception = Assertions.assertThrows(
      ResponseStatusException.class,
      () -> tenantService.deleteById(tenantId)
    );
    Assertions.assertEquals(
      AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString(),
      exception.getReason()
    );
  }

  @Test
  @DisplayName("Test getTenantConfig with valid tenantId")
  void testGetTenantConfigWithValidTenantId() {
    MockCurrentUserSession.setCurrentLoggedInUser(mockTenantID, null, null);
    // Mock the tenantConfigRepository
    List<TenantConfig> expectedConfigs = new ArrayList<>();
    Mockito
      .when(tenantConfigRepository.findAll(any(Specification.class)))
      .thenReturn(expectedConfigs);

    // Call the method under test
    List<TenantConfig> actualConfigs = tenantService.getTenantConfig(
      mockTenantID
    );

    // Verify the repository method was called
    Mockito.verify(tenantConfigRepository).findAll(any(Specification.class));

    // Verify the result
    Assertions.assertSame(expectedConfigs, actualConfigs);
  }

  @Test
  @DisplayName("Test getTenantConfig with invalid tenantId")
  void testGetTenantConfigWithInvalidTenantId() {
    UUID otherTenantId = MockTenantUser.INVALID_ID;
    MockCurrentUserSession.setCurrentLoggedInUser(otherTenantId, null, null);

    // Call the method under test and assert that it throws an exception
    Assertions.assertThrows(
      ResponseStatusException.class,
      () -> tenantService.getTenantConfig(mockTenantID),
      "Expected ResponseStatusException was not thrown."
    );

    // Verify that the repository method was not called
    Mockito.verifyNoInteractions(tenantConfigRepository);
  }
}
