package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenantPrefs;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserTenantPrefsRepository;
import com.sourcefuse.jarc.services.usertenantservice.specifications.UserTenantPrefsSpecification;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserTenantPrefsServiceImpl implements UserTenantPrefsService {

  private final UserTenantPrefsRepository userTPRepository;

  @Override
  public UserTenantPrefs createTenantPrefs(UserTenantPrefs userTenantPrefs) {
    UserTenantPrefs savedUserTenantPrefs;

    CurrentUser currentUser = (CurrentUser) SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getPrincipal();
    if (
      currentUser != null &&
      StringUtils.isNotEmpty(currentUser.getUserTenantId().toString())
    ) {
      userTenantPrefs.getUserTenant().setId(currentUser.getUserTenantId());
    }

    Optional<UserTenantPrefs> preExistsTenantPrefs = userTPRepository.findOne(
      UserTenantPrefsSpecification.byUserTenantIdAndConfigKey(
        userTenantPrefs.getUserTenant().getId(),
        userTenantPrefs.getConfigKey()
      )
    );
    if (preExistsTenantPrefs.isPresent()) {
      if (userTenantPrefs.getConfigValue() != null) {
        preExistsTenantPrefs
          .get()
          .setConfigValue(userTenantPrefs.getConfigValue());
      }
      savedUserTenantPrefs = userTPRepository.save(preExistsTenantPrefs.get());
    } else {
      savedUserTenantPrefs = userTPRepository.save(userTenantPrefs);
    }
    return savedUserTenantPrefs;
  }
}
