package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.core.models.session.CurrentUser;
import java.util.UUID;

public interface DeleteTntUserService {
  void deleteUserById(CurrentUser currentUser, UUID userId, UUID id);
}
