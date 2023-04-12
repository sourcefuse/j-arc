package com.sourcefuse.jarc.services.authservice.models;

import java.io.Serializable;
import java.util.UUID;
import org.springframework.data.redis.core.RedisHash;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@RedisHash("JwtTokenRedis")
public class JwtTokenRedis implements Serializable {

  @Id
  private UUID id;

  private String token;
}
