package com.sourcefuse.jarc.services.authservice.repositories;

import org.springframework.data.repository.CrudRepository;

import com.sourcefuse.jarc.services.authservice.models.RevokedTokenRedis;

public interface RevokedTokenRepository extends CrudRepository<RevokedTokenRedis, String> {
}
