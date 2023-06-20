package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.core.utils.CommonUtils;
import com.sourcefuse.jarc.services.usertenantservice.dto.Tenant;
import com.sourcefuse.jarc.services.usertenantservice.dto.TenantConfig;
import com.sourcefuse.jarc.services.usertenantservice.repository.TenantConfigRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.TenantRepository;
import com.sourcefuse.jarc.services.usertenantservice.specifications.TenantConfigSpecification;
import com.sourcefuse.jarc.services.usertenantservice.utils.CurrentUserUtils;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
@Slf4j
public class TenantServiceImpl implements TenantService {

  private final TenantRepository tenantRepository;
  private final TenantConfigRepository tenantConfigRepository;

  @Override
  public Tenant fetchTenantByID(UUID tenantId) {
    checkViewDeleteTenantAccessPermission(tenantId);
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
    checkViewDeleteTenantAccessPermission(tenantId);
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
    checkViewDeleteTenantAccessPermission(tenantId);
    tenantRepository.deleteById(tenantId);
  }

  @Override
  public List<TenantConfig> getTenantConfig(UUID tenantId) {
    checkViewDeleteTenantAccessPermission(tenantId);
    return tenantConfigRepository.findAll(
      TenantConfigSpecification.byTenantId(tenantId)
    );
  }

  private static void checkViewDeleteTenantAccessPermission(UUID tenantId) {
    CurrentUser currentUser = CurrentUserUtils.getCurrentUser();
    CurrentUserUtils.compareWithCurrentUserTenantId(tenantId, currentUser);
  }
}
