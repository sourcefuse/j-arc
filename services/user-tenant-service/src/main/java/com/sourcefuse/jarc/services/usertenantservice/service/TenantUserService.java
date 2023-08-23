package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.core.filters.models.Filter;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserDto;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface TenantUserService {
  UserDto create(
    UserDto userData,
    CurrentUser currentUser,
    Map<String, String> options
  );

  List<UserDto> getUserView(UUID uuid, CurrentUser currentUser,Filter filter);

  List<UserDto> getAllUsers(UUID tenantId, Filter filter);

  UserView findById(UUID userId, UUID tenantId, CurrentUser currentUser);
}
