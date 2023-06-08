package com.sourcefuse.jarc.services.authservice.services;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcefuse.jarc.core.exception.CommonRuntimeException;
import com.sourcefuse.jarc.services.authservice.enums.AuthErrorKeys;
import com.sourcefuse.jarc.services.authservice.models.JwtTokenRedis;
import com.sourcefuse.jarc.services.authservice.models.RefreshTokenRedis;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtRedisService {

  private final RedisTemplate<String, Object> redisTemplate;

  JwtTokenRedis getJwtTokenRedis(String code) {
    JwtTokenRedis jwtTokenObject = 
      (JwtTokenRedis) this.redisTemplate.opsForValue()
      .get(code);
    if (jwtTokenObject == null) {
      throw new CommonRuntimeException(
        HttpStatus.UNAUTHORIZED,
        AuthErrorKeys.CLIENT_INVALID.toString()
      );
    }
    return jwtTokenObject;
  }

  RefreshTokenRedis getRefreshTokenRedis(
    String refreshToken,
    String accessToken
  ) {
    RefreshTokenRedis refreshTokenRedis = 
      (RefreshTokenRedis) this.redisTemplate.opsForValue()
      .get(refreshToken);
    if (
      refreshTokenRedis == null ||
      !accessToken.equals(refreshTokenRedis.getExternalAuthToken())
    ) {
      throw new HttpServerErrorException(
        HttpStatus.UNAUTHORIZED,
        AuthErrorKeys.TOKEN_INVALID.toString()
      );
    }
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    return objectMapper.convertValue(
      refreshTokenRedis,
      RefreshTokenRedis.class
    );
  }
  void deleteRedisKeyById(String key) {
    this.redisTemplate.delete(key);
  }


  public void setWithTtl(String key, Object value, long ttl) {
    ValueOperations<String, Object> ops = redisTemplate.opsForValue();
    ops.set(key, value, ttl, TimeUnit.SECONDS);
  }
}
