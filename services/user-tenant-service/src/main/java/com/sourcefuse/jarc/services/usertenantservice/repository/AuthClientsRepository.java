package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.usertenantservice.dto.AuthClient;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthClientsRepository
  extends SoftDeletesRepository<AuthClient, UUID> {}
