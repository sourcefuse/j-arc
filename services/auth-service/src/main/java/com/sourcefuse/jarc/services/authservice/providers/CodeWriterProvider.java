package com.sourcefuse.jarc.services.authservice.providers;

import java.util.UUID;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.sourcefuse.jarc.services.authservice.models.JwtTokenRedis;
import lombok.AllArgsConstructor;

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
