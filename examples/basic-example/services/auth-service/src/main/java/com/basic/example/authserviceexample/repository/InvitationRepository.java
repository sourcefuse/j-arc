package com.basic.example.authserviceexample.repository;

import com.basic.example.authserviceexample.model.Invitation;
import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface InvitationRepository extends SoftDeletesRepository<Invitation, UUID> {
}
