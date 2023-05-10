package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.services.usertenantservice.dto.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
  User findByUsernameOrEmail(String username, String email);

  long countByIdNotAndUsername(UUID id, String username);
}
