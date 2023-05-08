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
@RedisHash("RefreshTokenRedis")
public class RefreshTokenRedis implements Serializable {

  @Id
  private String id;

  // private String token;
  private String clientId;
  private UUID userId;
  private String username;
  private String externalAuthToken;
  private String externalRefreshToken;
}
