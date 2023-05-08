package com.sourcefuse.jarc.services.authservice.models;

import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

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
