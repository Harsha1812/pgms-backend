package com.pgms.shared.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.UUID;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
      .csrf(csrf -> csrf.disable())

      .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )

      .authorizeHttpRequests(auth -> auth

        // Public endpoints
        .requestMatchers("/api/v1/auth/**").permitAll()
        .requestMatchers("/actuator/health", "/actuator/info").permitAll()

        // Platform endpoints
        .requestMatchers("/api/v1/platform/**")
        .hasRole("SUPER_ADMIN")

        // Admin endpoints
        .requestMatchers("/api/v1/admin/**")
        .hasAnyRole("OWNER_ADMIN", "BRANCH_MANAGER", "STAFF")

        // Tenant endpoints
        .requestMatchers("/api/v1/tenant/**")
        .hasRole("TENANT")

        // All other actuator endpoints secured
        .requestMatchers("/actuator/**")
        .hasRole("SUPER_ADMIN")

        .anyRequest().authenticated()
      )

      .addFilterBefore(jwtFilter,
        UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  CommandLineRunner printJwt(JwtService jwtService) {
    return args -> {
      String token = jwtService.generateToken(
        UUID.randomUUID(),
        UUID.randomUUID(),
        "OWNER_ADMIN"
      );
      System.out.println("\n\nREAL JWT TOKEN:");
      System.out.println(token);
      System.out.println("\n");
    };
  }
}