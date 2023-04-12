package com.sourcefuse.jarc.services.authservice.providers;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sourcefuse.jarc.services.authservice.models.JwtTokenRedis;
import com.sourcefuse.jarc.services.authservice.repositories.JwtTokenRedisRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CodeWriterProvider {

  JwtTokenRedisRepository jwtTokenRedisRepository;

  public UUID provide(String token) {
    UUID uuid = UUID.randomUUID();
    JwtTokenRedis jwtTokenRedis = new JwtTokenRedis(uuid, token);
    this.jwtTokenRedisRepository.save(jwtTokenRedis);

    return uuid;
  }
}
