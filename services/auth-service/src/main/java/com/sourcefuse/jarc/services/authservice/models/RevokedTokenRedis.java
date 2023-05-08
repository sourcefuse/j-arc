package com.sourcefuse.jarc.services.authservice.models;

import jakarta.persistence.Id;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RevokedTokenRedis implements Serializable {
  private static final long serialVersionUID = 1;
  @Id
  private String id;

  private String token;
}
