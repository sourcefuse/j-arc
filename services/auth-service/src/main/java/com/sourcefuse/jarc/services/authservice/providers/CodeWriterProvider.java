package com.sourcefuse.jarc.services.authservice.providers;

import com.sourcefuse.jarc.services.authservice.models.JwtTokenRedis;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CodeWriterProvider {

  private final RedisTemplate<String, Object> redisTemplate;

  public UUID provide(String token) {
    UUID uuid = UUID.randomUUID();
    JwtTokenRedis jwtTokenRedis = new JwtTokenRedis(uuid, token);

    redisTemplate.opsForValue().set(uuid.toString(), jwtTokenRedis);
    return uuid;
  }
}
