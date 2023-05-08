package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.services.usertenantservice.DTO.UserView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserViewRepository extends JpaRepository<UserView, UUID>, TenantRepositoryCustom {

    UserView findByUserTenantId(UUID id);
}
