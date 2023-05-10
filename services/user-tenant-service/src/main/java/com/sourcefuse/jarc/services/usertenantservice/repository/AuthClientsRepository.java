package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.services.usertenantservice.dto.AuthClient;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthClientsRepository
  extends JpaRepository<AuthClient, UUID>, TenantRepositoryCustom {}
