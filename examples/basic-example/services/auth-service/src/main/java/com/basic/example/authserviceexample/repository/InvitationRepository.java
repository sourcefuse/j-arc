package com.basic.example.authserviceexample.repository;

import com.basic.example.authserviceexample.model.Invitation;
import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository
  extends SoftDeletesRepository<Invitation, UUID> {}
