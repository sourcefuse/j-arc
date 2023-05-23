package com.sourcefuse.jarc.services.authservice.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.authservice.models.User;

public interface UserRepository  extends SoftDeletesRepository<User, UUID> {

  Optional<User> findByEmail(String email);

  Optional<User> findUserByUsername(String username);

  @Query(
    value = "SELECT u FROM User u WHERE u.username=:identity OR u.email=:identity"
  )
  Optional<User> findFirstUserByUsernameOrEmail(String identity);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
}
