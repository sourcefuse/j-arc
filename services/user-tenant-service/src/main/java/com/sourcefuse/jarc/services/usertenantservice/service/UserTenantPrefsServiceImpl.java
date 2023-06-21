package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenantPrefs;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserTenantPrefsRepository;
import com.sourcefuse.jarc.services.usertenantservice.specifications.UserTenantPrefsSpecification;
import com.sourcefuse.jarc.services.usertenantservice.utils.CurrentUserUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserTenantPrefsServiceImpl implements UserTenantPrefsService {

  private final UserTenantPrefsRepository userTenantPrefsRepository;

  @Override
  public UserTenantPrefs createTenantPrefs(UserTenantPrefs userTenantPrefs) {
    UserTenantPrefs savedUserTenantPrefs;

    CurrentUser currentUser = CurrentUserUtils.getCurrentUser();
    if (
      currentUser != null &&
      StringUtils.isNotEmpty(currentUser.getUserTenantId().toString())
    ) {
      userTenantPrefs.getUserTenant().setId(currentUser.getUserTenantId());
    }

    Optional<UserTenantPrefs> preExistsTenantPrefs =
      userTenantPrefsRepository.findOne(
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
      savedUserTenantPrefs =
        userTenantPrefsRepository.save(preExistsTenantPrefs.get());
    } else {
      savedUserTenantPrefs = userTenantPrefsRepository.save(userTenantPrefs);
    }
    return savedUserTenantPrefs;
  }
}
