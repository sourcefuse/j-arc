package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.services.usertenantservice.auth.IAuthUserWithPermissions;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenantPrefs;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserTenantPrefsRepository;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user-tenant-prefs")
public class UserTenantPrefsController {

  private final UserTenantPrefsRepository userTPRepository;

  @PostMapping("")
  public ResponseEntity<Object> createTenantPrefs(
    @Valid @RequestBody UserTenantPrefs userTenantPrefs
  ) {
    UserTenantPrefs savedUserTenantPrefs;

    IAuthUserWithPermissions currentUser =
      (IAuthUserWithPermissions) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();
    if (
      currentUser != null &&
      StringUtils.isNotEmpty(currentUser.getUserTenantId().toString())
    ) {
      userTenantPrefs.getUserTenant().setId(currentUser.getUserTenantId());
    }

    UserTenantPrefs preExistsTenantPrefs =
      userTPRepository.getByUserTenantIdAndConfigKey(
        userTenantPrefs.getUserTenant().getId(),
        userTenantPrefs.getConfigKey()
      );

    if (preExistsTenantPrefs != null) {
      if (userTenantPrefs.getConfigValue() != null) {
        preExistsTenantPrefs.setConfigValue(userTenantPrefs.getConfigValue());
      }
      savedUserTenantPrefs = userTPRepository.save(preExistsTenantPrefs);
    } else {
      savedUserTenantPrefs = userTPRepository.save(userTenantPrefs);
    }

    return new ResponseEntity<>(savedUserTenantPrefs, HttpStatus.CREATED);
  }

  @GetMapping("")
  public ResponseEntity<Object> getAllUsTenantPrefs() {
    List<UserTenantPrefs> userTenantPrefsList = userTPRepository.findAll();
    return new ResponseEntity<>(userTenantPrefsList, HttpStatus.OK);
  }
}
