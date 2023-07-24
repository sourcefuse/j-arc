package com.sourcefuse.jarc.authlib.utils;

import com.sourcefuse.jarc.authlib.security.CustomJwtBuilder;
import com.sourcefuse.jarc.core.constants.AuthConstants;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

public final class JwtUtils {

  private JwtUtils() {}

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

  public static Key key(String jwtSecret) {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }
}
