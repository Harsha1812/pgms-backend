package com.pgms.shared.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class JwtService {

  @Value("${jwt.secret}")
  private String secret;

  private static final long ACCESS_TOKEN_EXPIRY_MINUTES = 60;

  public String generateToken(UUID userId,
                              UUID ownerId,
                              String role,
                              List<UUID> branchIds,
                              UUID tenantId) {

    Map<String, Object> claims = new HashMap<>();
    claims.put("ownerId", ownerId != null ? ownerId.toString() : null);
    claims.put("role", role);
    claims.put("ver", 2);

    if (branchIds != null && !branchIds.isEmpty()) {
      claims.put("branchIds",
        branchIds.stream()
          .map(UUID::toString)
          .toList());
    } else {
      claims.put("branchIds", List.of());
    }

    if (tenantId != null) {
      claims.put("tenantId", tenantId.toString());
    }

    return Jwts.builder()
      .setSubject(userId.toString())
      .addClaims(claims)
      .setIssuedAt(new Date())
      .setExpiration(Date.from(
        Instant.now().plus(ACCESS_TOKEN_EXPIRY_MINUTES, ChronoUnit.MINUTES)))
      .signWith(SignatureAlgorithm.HS256, secret.getBytes())
      .compact();
  }

  public Claims validateToken(String token) {
    return Jwts.parser()
      .setSigningKey(secret.getBytes())
      .parseClaimsJws(token)
      .getBody();
  }
}