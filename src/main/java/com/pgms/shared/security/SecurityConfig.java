package com.pgms.shared.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.UUID;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
      .cors(withDefaults())
      .csrf(csrf -> csrf.disable())

      .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )

      .authorizeHttpRequests(auth -> auth

        // Public endpoints
        .requestMatchers(
          "/api/v1/auth/**",
          "/actuator/health",
          "/actuator/info",
          "/api/v1/platform/owners/register",
          "/v3/api-docs/**",
          "/swagger-ui/**",
          "/swagger-ui.html"
        ).permitAll()
        // Admin endpoints
        .requestMatchers("/api/v1/admin/**")
        .hasAnyRole("OWNER_ADMIN", "BRANCH_MANAGER", "STAFF")

        // Tenant endpoints
        .requestMatchers("/api/v1/tenant/**")
        .hasRole("TENANT")

        // All other actuator endpoints secured
        .requestMatchers("/actuator/**")
        .hasRole("SUPER_ADMIN")

          // Platform endpoints
          .requestMatchers("/api/v1/platform/**")
          .hasRole("SUPER_ADMIN")

        .anyRequest().authenticated()
      )

      .addFilterBefore(jwtFilter,
        UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:5173"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
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
      System.out.println(new BCryptPasswordEncoder().encode("password123"));
    };
  }
}