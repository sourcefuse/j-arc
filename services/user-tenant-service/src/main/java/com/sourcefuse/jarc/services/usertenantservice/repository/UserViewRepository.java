package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface UserViewRepository
  extends
    SoftDeletesRepository<UserView, UUID>, TenantRepositoryCustom<UserView> {
  UserView findByUserTenantId(UUID id);
}
