package com.pgms.shared.config;

import com.pgms.shared.security.BranchAuthorizationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final BranchAuthorizationInterceptor branchAuthorizationInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(branchAuthorizationInterceptor)
      .addPathPatterns("/api/v1/**");
  }
}