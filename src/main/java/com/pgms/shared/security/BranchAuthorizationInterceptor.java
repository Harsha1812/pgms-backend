package com.pgms.shared.security;

import com.pgms.shared.security.dto.AuthUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.UUID;

@Component
public class BranchAuthorizationInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler) throws Exception {

    if (!(handler instanceof HandlerMethod)) {
      return true;
    }

    Authentication authentication =
      SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !(authentication.getPrincipal() instanceof AuthUser authUser)) {
      return true;
    }

    @SuppressWarnings("unchecked")
    var pathVariables = (java.util.Map<String, String>)
      request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

    if (pathVariables == null || !pathVariables.containsKey("branchId")) {
      return true;
    }

    UUID requestedBranchId;

    try {
      requestedBranchId = UUID.fromString(pathVariables.get("branchId"));
    } catch (IllegalArgumentException e) {
      return true;
    }

    if (authUser.role().equals("OWNER_ADMIN") ||
      authUser.role().equals("SUPER_ADMIN")) {
      return true;
    }

    if (!authUser.branchIds().contains(requestedBranchId)) {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.setContentType("application/json");
      response.getWriter().write("""
            {
              "success": false,
              "error": {
                "code": "FORBIDDEN",
                "message": "Access denied for this branch",
                "details": null
              }
            }
        """);
      return false;
    }

    return true;
  }
}