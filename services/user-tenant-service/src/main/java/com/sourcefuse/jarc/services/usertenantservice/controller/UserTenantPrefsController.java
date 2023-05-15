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

/**
 * doubt::
 * filter and authentication pending
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user-tenant-prefs")
public class UserTenantPrefsController {

  private final UserTenantPrefsRepository userTPRepository;

  @PostMapping("")
  public ResponseEntity<Object> createTenantPrefs(
    @Valid @RequestBody UserTenantPrefs userTPrefs
  ) {
    UserTenantPrefs savedUTPrefs;

    IAuthUserWithPermissions currentUser =
      (IAuthUserWithPermissions) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();
    if (
      currentUser != null &&
      StringUtils.isNotEmpty(currentUser.getUserTenantId().toString())
    ) {
      userTPrefs.getUserTenant().setId(currentUser.getUserTenantId());
    }

    UserTenantPrefs preExistsTenantPrefs =
      userTPRepository.findByUserTenantIdAndConfigKey(
        userTPrefs.getUserTenant().getId(),
        userTPrefs.getConfigKey().getValue()
      );

    if (preExistsTenantPrefs != null) {
      if (userTPrefs.getConfigValue() != null) {
        preExistsTenantPrefs.setConfigValue(userTPrefs.getConfigValue());
      }
      savedUTPrefs = userTPRepository.save(preExistsTenantPrefs);
    } else {
      savedUTPrefs = userTPRepository.save(userTPrefs);
    }

    return new ResponseEntity<>(savedUTPrefs, HttpStatus.CREATED);
  }

  @GetMapping("")
  public ResponseEntity<Object> getAllUsTenantPrefs() {
    List<UserTenantPrefs> listUtPrefs = userTPRepository.findAll();
    return new ResponseEntity<>(listUtPrefs, HttpStatus.OK);
  }
}
