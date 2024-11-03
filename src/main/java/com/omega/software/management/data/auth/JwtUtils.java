package com.omega.software.management.data.auth;

import com.omega.software.management.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${management.app.jwtSecret}")
  private String jwtSecret;

  @Value("${management.app.jwtExpirationMs}")
  private int jwtExpirationMs;


  public String generateJwt(UserDetailsImpl userPrincipal) {
    String jwt = generateTokenFromEmail(userPrincipal.getEmail());
    return jwt;
  }

  public String getUserEmailFromJwtToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key()).build()
               .parseClaimsJws(token).getBody().getSubject();
  }

  public boolean isExpired(String token) {
     return ((Claims) Jwts.parser().parse(token).getBody())
             .getExpiration()
             .toInstant()
             .isAfter(Instant.now());
  }

  public Instant getExpirationTime(String token) {
    return ((Claims) Jwts.parserBuilder().setSigningKey(key()).build()
            .parse(token).getBody())
            .getExpiration().toInstant();
  }


  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
      return true;
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
      throw new JwtException("Invalid JWT token: %s".formatted(e.getMessage()));
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
      throw new JwtException("JWT token is expired: %s".formatted(e.getMessage()));
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
      throw new JwtException("JWT token is unsupported: %s".formatted(e.getMessage()));
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
      throw new JwtException("JWT claims string is empty: %s".formatted(e.getMessage()));
    }
  }

  public String generateTokenFromEmail(String email) {
    return Jwts.builder()
               .setSubject(email)
               .setIssuedAt(new Date())
               .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
               .signWith(key(), SignatureAlgorithm.HS256)
               .compact();
  }
}
