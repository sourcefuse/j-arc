package com.sourcefuse.jarc.services.authservice.repositories;

import org.springframework.data.repository.CrudRepository;
import com.sourcefuse.jarc.services.authservice.models.JwtTokenRedis;

public interface JwtTokenCodeRedisRepository extends CrudRepository<JwtTokenRedis, String> {
}
