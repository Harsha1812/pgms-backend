package com.pgms.shared.security;

import com.pgms.branch.repository.entity.Branch;
import com.pgms.shared.security.dto.AuthUser;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
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
      if (!List.of("OWNER_ADMIN", "BRANCH_MANAGER", "STAFF", "TENANT", "SUPER_ADMIN")
        .contains(role)) {
        throw new RuntimeException("Invalid role in token");
      }



      // Owner may be null for tenant
      String ownerIdStr = claims.get("ownerId", String.class);
      if (ownerIdStr == null) {
        throw new RuntimeException("Token missing ownerId");
      }
      UUID ownerId = UUID.fromString(ownerIdStr);
      Object branchIdsClaim = claims.get("branchIds");
      Set<UUID> branchIds = new HashSet<>();

      if (branchIdsClaim instanceof List<?> rawList) {
        branchIds = rawList.stream()
          .filter(String.class::isInstance)
          .map(String.class::cast)
          .map(UUID::fromString)
          .collect(Collectors.toSet());
      }

      String tenantIdStr = claims.get("tenantId", String.class);
      UUID tenantId = tenantIdStr != null ? UUID.fromString(tenantIdStr) : null;
      if (tenantId != null) {
        TenantContext.setTenantId(tenantId);
      }
      Integer version = claims.get("ver", Integer.class);
      if (version == null || version != 2) {
        throw new RuntimeException("Unsupported token version");
      }

      if ((role.equals("BRANCH_MANAGER") || role.equals("STAFF") || role.equals("TENANT"))
        && branchIds.isEmpty()) {
        throw new RuntimeException("Branch-based role without branches");
      }
      List<GrantedAuthority> authorities =
        List.of(new SimpleGrantedAuthority("ROLE_" + role));
      // Populate SecurityContext
      AuthUser authUser = new AuthUser(
        userId,
        ownerId,
        branchIds,
        tenantId,
        role
      );

      UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(
          authUser,
          null,
          authorities
        );

      SecurityContextHolder.getContext().setAuthentication(authentication);

      // Populate OwnerContext only for owner-based roles
      OwnerContext.setOwnerId(ownerId);
      MDC.put("ownerId", ownerId.toString());


      if (!branchIds.isEmpty()) {
        BranchContext.setBranchIds(branchIds);
      }

      MDC.put("userId", userId.toString());
      MDC.put("role", role);
      MDC.put("branchIds", branchIds.toString());

      filterChain.doFilter(request, response);

    } catch (Exception ex) {
      log.warn("JWT validation failed: {}", ex.getMessage());

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
      BranchContext.clear();
      TenantContext.clear();
      MDC.clear();
    }
  }
}