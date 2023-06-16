package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import jakarta.validation.Valid;
import java.util.UUID;

public interface UpdateTenantUserService {
  void updateById(
    CurrentUser currentUser,
    UUID userId,
    @Valid UserView userView,
    UUID id
  );
}
