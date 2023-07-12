package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.core.constants.CommonConstants;
import com.sourcefuse.jarc.core.enums.PermissionKey;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.core.utils.CommonUtils;
import com.sourcefuse.jarc.services.usertenantservice.dto.Tenant;
import com.sourcefuse.jarc.services.usertenantservice.dto.TenantConfig;
import com.sourcefuse.jarc.services.usertenantservice.enums.AuthorizeErrorKeys;
import com.sourcefuse.jarc.services.usertenantservice.repository.TenantConfigRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.TenantRepository;
import com.sourcefuse.jarc.services.usertenantservice.specifications.TenantConfigSpecification;
import com.sourcefuse.jarc.services.usertenantservice.utils.CurrentUserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class TenantServiceImpl implements TenantService {

  private final TenantRepository tenantRepository;
  private final TenantConfigRepository tenantConfigRepository;

  @Override
  public Tenant fetchTenantByID(UUID tenantId) {
    checkViewDeleteTenantAccessPermission(tenantId, CommonConstants.VIEW);
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
    checkViewDeleteTenantAccessPermission(tenantId, CommonConstants.UPDATE);
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
    checkViewDeleteTenantAccessPermission(tenantId, CommonConstants.DELETE);
    tenantRepository.deleteById(tenantId);
  }

  @Override
  public List<TenantConfig> getTenantConfig(UUID tenantId) {
    checkViewDeleteTenantAccessPermission(tenantId, CommonConstants.VIEW);
    return tenantConfigRepository.findAll(
      TenantConfigSpecification.byTenantId(tenantId)
    );
  }

  private static void checkViewDeleteTenantAccessPermission(
    UUID tenantId,
    String operationType
  ) {
    CurrentUser currentUser = CurrentUserUtils.getCurrentUser();

    if (CommonConstants.VIEW.equalsIgnoreCase(operationType)) {
      checkForViewOwnUserPermission(currentUser, tenantId);
    } else if (CommonConstants.UPDATE.equalsIgnoreCase(operationType)) {
      checkForUpdateOwnUserPermission(currentUser, tenantId);
    } else if (CommonConstants.DELETE.equalsIgnoreCase(operationType)) {
      CurrentUserUtils.compareWithCurrentUserTenantId(tenantId, currentUser);
    } else{
      log.info("Else block to handle future condition if any !");
    }
  }

  private static void checkForViewOwnUserPermission(
    CurrentUser currentUser,
    UUID tenantId
  ) {
    if (
      !currentUser
        .getPermissions()
        .contains(PermissionKey.VIEW_OWN_TENANT.toString()) &&
      !tenantId.equals(currentUser.getTenantId())
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
      );
    }
  }

  private static void checkForUpdateOwnUserPermission(
    CurrentUser currentUser,
    UUID tenantId
  ) {
    if (
      !currentUser
        .getPermissions()
        .contains(PermissionKey.UPDATE_OWN_TENANT.toString()) &&
      !tenantId.equals(currentUser.getTenantId())
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
      );
    }
  }
}
