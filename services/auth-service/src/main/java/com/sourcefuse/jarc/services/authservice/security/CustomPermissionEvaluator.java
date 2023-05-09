package com.sourcefuse.jarc.services.authservice.security;

import java.io.Serializable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {

  @Override
  public boolean hasPermission(
    Authentication authentication,
    Object targetDomainObject,
    Object permission
  ) {
    // ABAC implementation goes here
    return true;
  }

  @Override
  public boolean hasPermission(
    Authentication authentication,
    Serializable targetId,
    String targetType,
    Object permission
  ) {
    // ABAC implementation goes here
    return true;
  }
}
