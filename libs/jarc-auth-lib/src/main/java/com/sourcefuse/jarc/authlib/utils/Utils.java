package com.sourcefuse.jarc.authlib.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sourcefuse.jarc.authlib.security.CustomJwtBuilder;
import com.sourcefuse.jarc.core.constants.AuthConstants;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public final class Utils {

  private Utils() {}

  public static ObjectMapper getObjectMapperInstance() {
    return new Jackson2ObjectMapperBuilder()
      .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .modulesToInstall(new JavaTimeModule())
      .build();
  }

  public static String generateAccessToken(
    String jwtSecret,
    Long jwtExpirationDate,
    CurrentUser currentUser
  ) {
    Date currentDate = new Date();
    Date expireDate = new Date();
    expireDate.setTime(currentDate.getTime() + jwtExpirationDate);
    Claims claims = Jwts.claims().setSubject(currentUser.getUsername());
    claims.put(AuthConstants.CURRENT_USER_KEY, currentUser);
    return new CustomJwtBuilder()
      .setClaims(claims)
      .setIssuedAt(currentDate)
      .setExpiration(expireDate)
      .signWith(key(jwtSecret))
      .compact();
  }

  private static Key key(String jwtSecret) {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }
}
