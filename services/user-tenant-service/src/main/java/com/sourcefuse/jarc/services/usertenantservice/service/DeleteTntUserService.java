package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.services.usertenantservice.auth.IAuthUserWithPermissions;
import java.util.UUID;

public interface DeleteTntUserService {
  void deleteUserById(
    IAuthUserWithPermissions currentUser,
    UUID userId,
    UUID id
  );
}
