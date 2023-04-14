package com.sourcefuse.jarc.services.authservice.providers;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcefuse.jarc.services.authservice.Constants;
import com.sourcefuse.jarc.services.authservice.exception.CommonRuntimeException;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.session.CurrentUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

  @Value("${app.jwt-secret}")
  private String jwtSecret;

  @Value("${app-jwt-expiration-milliseconds}")
  private long jwtExpirationDate;

  // generate JWT token
  public String generateToken(User user) {
    String username = user.getUsername();
    CurrentUser currentUser = new CurrentUser(user);

    Date currentDate = new Date();

    Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
    Claims claims = Jwts.claims().setSubject(username);
    claims.put(Constants.CURRENT_USER_KEY, currentUser);
    return Jwts
        .builder()
        .setClaims(claims)
        .setIssuedAt(new Date())
        .setExpiration(expireDate)
        .signWith(key())
        .compact();
  }

  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  // get username from Jwt token
  public CurrentUser getUserDetails(String token) {
    Claims claims = Jwts
        .parserBuilder()
        .setSigningKey(key())
        .build()
        .parseClaimsJws(token)
        .getBody();
    Object userObject = claims.get(Constants.CURRENT_USER_KEY);
    ObjectMapper mapper = new ObjectMapper();
    return mapper.convertValue(userObject, CurrentUser.class);
  }

  // validate Jwt token
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
      return true;
    } catch (MalformedJwtException ex) {
      throw new CommonRuntimeException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      throw new CommonRuntimeException(HttpStatus.BAD_REQUEST, "Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      throw new CommonRuntimeException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      throw new CommonRuntimeException(
          HttpStatus.BAD_REQUEST,
          "JWT claims string is empty.");
    }
  }
}
