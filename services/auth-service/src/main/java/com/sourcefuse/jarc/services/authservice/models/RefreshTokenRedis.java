package com.sourcefuse.jarc.services.authservice.models;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.redis.core.RedisHash;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
