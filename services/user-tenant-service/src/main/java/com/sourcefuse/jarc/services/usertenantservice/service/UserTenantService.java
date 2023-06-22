package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import java.util.UUID;

public interface UserTenantService {
  UserView getUserTenantById(UUID id);
}
