package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.core.utils.CommonUtils;
import com.sourcefuse.jarc.services.usertenantservice.commons.CurrentUserUtils;
import com.sourcefuse.jarc.services.usertenantservice.dto.Tenant;
import com.sourcefuse.jarc.services.usertenantservice.dto.TenantConfig;
import com.sourcefuse.jarc.services.usertenantservice.repository.TenantConfigRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.TenantRepository;
import com.sourcefuse.jarc.services.usertenantservice.specifications.TenantConfigSpecification;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class TenantServiceImpl implements TenantService {

  private final TenantRepository tenantRepository;
  private final TenantConfigRepository tenantConfigRepository;

  @Override
  public Tenant fetchTenantByID(UUID tenantId) {
    checkViewTenantAccessPermission(tenantId);
    return tenantRepository
      .findById(tenantId)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "No tenant is present against given value"
        )
      );
  }

  @Override
  public void updateTenantsById(Tenant sourceTenant, UUID tenantId) {
    checkViewTenantAccessPermission(tenantId);
    Tenant targetTenant = tenantRepository
      .findById(tenantId)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "No tenant is present against given value"
        )
      );
    BeanUtils.copyProperties(
      sourceTenant,
      targetTenant,
      CommonUtils.getNullPropertyNames(sourceTenant)
    );
    tenantRepository.save(targetTenant);
  }

  @Override
  public void deleteById(UUID tenantId) {
    checkDeleteTenantAccessPermission(tenantId);
    tenantRepository.deleteById(tenantId);
  }

  @Override
  public List<TenantConfig> getTenantConfig(UUID tenantId) {
    checkViewTenantAccessPermission(tenantId);
    return tenantConfigRepository.findAll(
      TenantConfigSpecification.byTenantId(tenantId)
    );
  }

  private static void checkViewTenantAccessPermission(UUID tenantId) {
    CurrentUser currentUser = CurrentUserUtils.getCurrentUser();
    CurrentUserUtils.compareWithCurrentUserTenantId(tenantId, currentUser);
  }

  private static void checkDeleteTenantAccessPermission(UUID tenantId) {
    CurrentUser currentUser = CurrentUserUtils.getCurrentUser();
    CurrentUserUtils.compareWithCurrentUserTenantId(tenantId, currentUser);
  }
}
