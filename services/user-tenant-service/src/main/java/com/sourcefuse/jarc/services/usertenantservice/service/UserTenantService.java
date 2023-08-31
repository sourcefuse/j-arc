package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.core.filters.models.Filter;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenant;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public interface UserTenantService {
  UserView getUserTenantById(UUID id);

  Specification<UserTenant> getUserTenantSpecification(UUID id, Filter filter);
}
