package com.sourcefuse.jarc.services.authservice.models;

import java.io.Serializable;

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
@RedisHash("RevokedTokenRedis")
public class RevokedTokenRedis implements Serializable {

  @Id
  private String id;
  private String token;

}
