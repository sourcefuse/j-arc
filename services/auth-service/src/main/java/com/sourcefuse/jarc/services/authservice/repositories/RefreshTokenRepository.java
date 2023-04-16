package com.sourcefuse.jarc.services.authservice.repositories;

import org.springframework.data.repository.CrudRepository;
import com.sourcefuse.jarc.services.authservice.models.RefreshTokenRedis;

public interface RefreshTokenRepository extends CrudRepository<RefreshTokenRedis, String> {
}
