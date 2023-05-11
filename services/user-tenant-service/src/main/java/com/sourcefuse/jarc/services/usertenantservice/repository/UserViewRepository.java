package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserViewRepository
  extends JpaRepository<UserView, UUID>, TenantRepositoryCustom<UserView> {
  UserView findByUserTenantId(UUID id);
}
