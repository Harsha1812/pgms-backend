package com.pgms.shared.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  private static final String AUTH_PREFIX = "Bearer ";

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();

    // Skip public endpoints
    return path.startsWith("/api/v1/auth")
      || path.startsWith("/actuator/health")
      || path.startsWith("/actuator/info");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
    throws ServletException, IOException {

    try {

      String header = request.getHeader(HttpHeaders.AUTHORIZATION);

      if (header == null || !header.startsWith(AUTH_PREFIX)) {
        filterChain.doFilter(request, response);
        return;
      }

      String token = header.substring(AUTH_PREFIX.length());

      Claims claims = jwtService.validateToken(token);

      UUID userId = UUID.fromString(claims.getSubject());
      String role = claims.get("role", String.class);

      // Owner may be null for tenant
      String ownerIdStr = claims.get("ownerId", String.class);
      UUID ownerId = ownerIdStr != null ? UUID.fromString(ownerIdStr) : null;

      // Populate SecurityContext
      UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(
          userId,
          null,
          List.of(new SimpleGrantedAuthority("ROLE_" + role))
        );

      SecurityContextHolder.getContext().setAuthentication(authentication);

      // Populate OwnerContext only for owner-based roles
      if (ownerId != null) {
        OwnerContext.setOwnerId(ownerId);
        MDC.put("ownerId", ownerId.toString());
      }

      MDC.put("userId", userId.toString());
      MDC.put("role", role);

      filterChain.doFilter(request, response);

    } catch (Exception ex) {

      SecurityContextHolder.clearContext();
      OwnerContext.clear();

      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.getWriter().write("""
                {
                  "success": false,
                  "error": {
                    "code": "UNAUTHORIZED",
                    "message": "Invalid or expired token",
                    "details": null
                  }
                }
                """);
      return;

    } finally {
      OwnerContext.clear();
      MDC.clear();
    }
  }
}