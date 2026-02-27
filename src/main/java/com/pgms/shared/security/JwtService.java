package com.pgms.shared.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

  @Value("${jwt.secret}")
  private String secret;

  public Claims validateToken(String token) {
    return Jwts.parser()
      .setSigningKey(secret.getBytes())
      .parseClaimsJws(token)
      .getBody();
  }

  public String generateToken(UUID userId,
                              UUID ownerId,
                              String role) {

    return Jwts.builder()
      .setSubject(userId.toString())
      .claim("ownerId", ownerId != null ? ownerId.toString() : null)
      .claim("role", role)
      .setIssuedAt(new Date())
      .setExpiration(Date.from(
        Instant.now().plus(1, ChronoUnit.HOURS)))
      .signWith(SignatureAlgorithm.HS256, secret.getBytes())
      .compact();
  }
}