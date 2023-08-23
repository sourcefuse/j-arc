package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.core.constants.PermissionKeyConstants;
import com.sourcefuse.jarc.core.filters.models.Filter;
import com.sourcefuse.jarc.core.filters.services.QueryService;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenantPrefs;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserTenantPrefsRepository;
import com.sourcefuse.jarc.services.usertenantservice.service.UserTenantPrefsService;
import com.sourcefuse.jarc.services.usertenantservice.specifications.UserTenantPrefsSpecification;
import com.sourcefuse.jarc.services.usertenantservice.utils.CurrentUserUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user-tenant-prefs")
@SecurityRequirement(name = "bearerAuth")
public class UserTenantPrefsController {

  private final UserTenantPrefsRepository userTenantPrefsRepository;
  private final UserTenantPrefsService userTenantPrefsService;
  private final QueryService queryService;

  @PostMapping
  @PreAuthorize(
    "isAuthenticated() && hasAuthority('" +
    PermissionKeyConstants.UPDATE_USER_TENANT_PREFERENCE +
    "')"
  )
  public ResponseEntity<UserTenantPrefs> createTenantPrefs(
    @Valid @RequestBody UserTenantPrefs userTenantPrefs
  ) {
    return new ResponseEntity<>(
      userTenantPrefsService.createTenantPrefs(userTenantPrefs),
      HttpStatus.CREATED
    );
  }

  @GetMapping
  @PreAuthorize(
    "isAuthenticated() && hasAuthority('" +
    PermissionKeyConstants.VIEW_USER_TENANT_PREFERENCE +
    "')"
  )
  public ResponseEntity<List<UserTenantPrefs>> getAllUsTenantPrefs(
    @RequestParam(required = false, name = "filter") Filter filter
  ) {
    CurrentUser currentUser = CurrentUserUtils.getCurrentUser();

    Specification<UserTenantPrefs> userTenantPrefSpecifications =
      queryService.getSpecifications(filter);
    userTenantPrefSpecifications =
      userTenantPrefSpecifications.and(
        UserTenantPrefsSpecification.byUserTenantId(
          currentUser.getUserTenantId()
        )
      );

    return new ResponseEntity<>(
      userTenantPrefsRepository.findAll(userTenantPrefSpecifications),
      HttpStatus.OK
    );
  }
}
