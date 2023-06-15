package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.services.usertenantservice.dto.Tenant;
import com.sourcefuse.jarc.services.usertenantservice.dto.TenantConfig;
import java.util.List;
import java.util.UUID;

public interface TenantService {
  Tenant fetchTenantByID(UUID tenantId);

  void updateTenantsById(Tenant sourceTenant, UUID tenantId);

  void deleteById(UUID tenantId);

  List<TenantConfig> getTenantConfig(UUID tenantId);
}
