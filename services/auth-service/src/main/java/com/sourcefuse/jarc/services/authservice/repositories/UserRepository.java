package com.sourcefuse.jarc.services.authservice.repositories;

import com.sourcefuse.jarc.services.authservice.models.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, UUID> {
  List<User> findByLastName(String lastName);

  Optional<User> findByEmail(String email);

  Optional<User> findUserByUsername(String username);

  @Query(
    value = "SELECT u FROM User u WHERE u.username=:identity OR u.email=:identity"
  )
  Optional<User> findFirstUserByUsernameOrEmail(String identity);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
}
