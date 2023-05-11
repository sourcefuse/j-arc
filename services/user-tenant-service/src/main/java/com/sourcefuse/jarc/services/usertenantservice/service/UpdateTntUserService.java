package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.services.usertenantservice.auth.IAuthUserWithPermissions;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import jakarta.validation.Valid;
import java.util.UUID;

public interface UpdateTntUserService {
  void updateById(
    IAuthUserWithPermissions currentUser,
    UUID userId,
    @Valid UserView userView,
    UUID id
  );
}
