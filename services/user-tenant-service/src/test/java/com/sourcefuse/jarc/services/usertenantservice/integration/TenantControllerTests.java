package com.sourcefuse.jarc.services.usertenantservice.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.sourcefuse.jarc.services.usertenantservice.controller.TenantController;
import com.sourcefuse.jarc.services.usertenantservice.dto.Tenant;
import com.sourcefuse.jarc.services.usertenantservice.dto.TenantConfig;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockCurrentUserSession;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockTenantUser;
import com.sourcefuse.jarc.services.usertenantservice.mocks.JsonUtils;
import com.sourcefuse.jarc.services.usertenantservice.repository.TenantRepository;
import com.sourcefuse.jarc.services.usertenantservice.service.TenantService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

@DisplayName("Create Tenant  Apis Integration/units Tests")
@ExtendWith(MockitoExtension.class)
class TenantControllerTests {

  @Mock
  private TenantRepository tenantRepository;

  @InjectMocks
  private TenantController tenantController;

  private Tenant tenant;
  private UUID mockTenantId;

  private MockMvc mockMvc;
  private String basePath = "/tenants";

  @Mock
  private TenantService tenantService;

  @BeforeEach
  public void setup() {
    //prepare the test data
    mockMvc = MockMvcBuilders.standaloneSetup(tenantController).build();
    tenant = MockTenantUser.geTenantObj();
    mockTenantId = MockTenantUser.TENANT_ID;
    //Set Current LoggedIn User
    MockCurrentUserSession.setCurrentLoggedInUser(null, null, null);
  }

  @Test
  @DisplayName("Create Tenant - Success")
  void testCreateTenant_Success() throws Exception {
    // Arrange
    Tenant savedTenant = this.tenant;

    Mockito.when(tenantRepository.save(tenant)).thenReturn(savedTenant);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(tenant))
      )
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(
        MockMvcResultMatchers
          .jsonPath("$.id")
          .value(savedTenant.getId().toString())
      )
      .andExpect(
        MockMvcResultMatchers.jsonPath("$.name").value(savedTenant.getName())
      );

    Mockito.verify(tenantRepository, times(1)).save(tenant);
  }

  @Test
  @DisplayName("Test: Should pass with invalid name")
  void testCreateTenant_InvalidInput_EmptyName() throws Exception {
    // Prepare test data with invalid input
    tenant.setName("");

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(tenant))
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(result ->
        Assertions.assertTrue(
          result.getResolvedException() instanceof MethodArgumentNotValidException
        )
      );

    Mockito.verify(tenantRepository, (never())).save(tenant);
  }

  @Test
  @DisplayName("Test: Should pass with invalid TenantStatus")
  void testCreateTenant_InvalidInput_TenantStatus() throws Exception {
    // Prepare test data with invalid input
    tenant.setStatus(null);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(tenant))
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(result ->
        Assertions.assertTrue(
          result.getResolvedException() instanceof MethodArgumentNotValidException
        )
      );

    Mockito.verify(tenantRepository, (never())).save(tenant);
  }

  @Test
  @DisplayName("Test: case for count success")
  void testCount_Success() throws Exception {
    when(tenantRepository.count()).thenReturn(5L); // Mock a count of 5

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
    Mockito.verify(tenantRepository).count();
  }

  @Test
  @DisplayName("Test case should pass for 0 count")
  void testCount_Empty() throws Exception {
    // Mock the behavior of roleRepository.count()
    when(tenantRepository.count()).thenReturn(0L); // Mock a count of 0

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
    Mockito.verify(tenantRepository).count();
  }

  @Test
  @DisplayName("Test getAllTenants success")
  void testGetAllTenants_Success() throws Exception {
    // Prepare test data
    List<Tenant> tenants = Arrays.asList(
      new Tenant(),
      new Tenant(),
      new Tenant()
    );

    // Mock the behavior of roleRepository.findAll()
    when(tenantRepository.findAll()).thenReturn(tenants);

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
    Mockito.verify(tenantRepository).findAll();
  }

  @Test
  @DisplayName("Test: getAllTenants Empty Response")
  void testGetAllTenants_Empty() throws Exception {
    // Mock the behavior of roleRepository.findAll()
    when(tenantRepository.findAll()).thenReturn(Arrays.asList());

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
    Mockito.verify(tenantRepository).findAll();
  }

  @Test
  @DisplayName("Test updateAll success")
  void testUpdateAll_Success() throws Exception {
    // Prepare test data
    tenant.setName("Update Name");

    List<Tenant> targetListTenant = Arrays.asList(
      new Tenant(),
      new Tenant(),
      new Tenant()
    );

    // Mock the behavior of roleRepository.findAll()
    when(tenantRepository.findAll()).thenReturn(targetListTenant);

    // Mock the behavior of roleRepository.saveAll()
    when(tenantRepository.saveAll(any())).thenReturn(targetListTenant);

    // Perform the API call
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .patch(basePath)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(tenant))
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(result -> Assertions.assertNotNull(result.getResponse()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(3));

    // Verify that roleRepository.findAll() was called
    Mockito.verify(tenantRepository).findAll();

    // Verify that roleRepository.saveAll() was called with the updated roles
    Mockito.verify(tenantRepository).saveAll(targetListTenant);
  }

  @Test
  @DisplayName("Test: updateAll : No existing records found ")
  void testUpdateAll_Empty() throws Exception {
    tenant.setName("Updated Name");

    // Mock the behavior of roleRepository.findAll()
    when(tenantRepository.findAll()).thenReturn(new ArrayList<>());

    // Perform the API call
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .patch("/tenants")
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(tenant))
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(result -> Assertions.assertNotNull(result.getResponse()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(0));
    // Verify that roleRepository.findAll() was called
    Mockito.verify(tenantRepository).findAll();

    // Verify that roleRepository.saveAll() was not called
    Mockito.verify(tenantRepository, never()).saveAll(any());
  }

  @Test
  @DisplayName("Test fetchTenantById Success Integration Test")
  void testFetchTenantByID() throws Exception {
    // Mock the tenant repository
    when(tenantService.fetchTenantByID(mockTenantId)).thenReturn(tenant);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get("/tenants/{id}", mockTenantId)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk());

    // Verify that the repository method was called
    Mockito.verify(tenantService, times(1)).fetchTenantByID(mockTenantId);
  }

  @Test
  @DisplayName("Test Update Tenant - Success Integration ")
  void testUpdateTenant_Success() throws Exception {
    // Arrange
    Tenant existingTenant = new Tenant(mockTenantId);
    Tenant sourceTenant = this.tenant;

    Mockito
      .doNothing()
      .when(tenantService)
      .updateTenantsById(sourceTenant, mockTenantId);

    // Act
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .patch("/tenants/{id}", mockTenantId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JsonUtils.asJsonString(sourceTenant))
      )
      .andExpect(MockMvcResultMatchers.status().isNoContent())
      .andExpect(
        MockMvcResultMatchers.jsonPath("$").value("Tenant PATCH success")
      );

    // verify method calls
    Mockito
      .verify(tenantService, times(1))
      .updateTenantsById(sourceTenant, mockTenantId);
  }

  @Test
  @DisplayName("Test:Delete  Tenant by ID Success  Integration")
  void testDeleteTenantById_ExistingRole_Success() throws Exception {
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .delete("/tenants/{id}", mockTenantId)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isNoContent())
      .andExpect(
        MockMvcResultMatchers.jsonPath("$").value("Tenant DELETE success")
      );

    Mockito.verify(tenantService, times(1)).deleteById(mockTenantId);
  }

  @Test
  @DisplayName("Get Tenant Config - Success Integration ")
  void testGetTenantConfig_Success() throws Exception {
    ArrayList<TenantConfig> expectedConfig = new ArrayList<>();
    expectedConfig.add(new TenantConfig());
    expectedConfig.add(new TenantConfig());

    when(tenantService.getTenantConfig(any(UUID.class)))
      .thenReturn(expectedConfig);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get("/tenants/{id}/config", mockTenantId)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2));

    Mockito.verify(tenantService, times(1)).getTenantConfig(any(UUID.class));
  }
}
