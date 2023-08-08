package com.sourcefuse.jarc.authlib.api.security.types;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class ApiSecurityConstants {

  public static final String SELF = "'self'";

  // 180 * 24 * 60 * 60 = 15552000
  public static final Double DEFAULT_MAX_AGE = 15552000D;

  @SuppressWarnings("unchecked")
  public static final Map<String, Object> DEFAULT_CONTENT_SECURITY_POLICY_DIRECTIVE =
    new HashMap<>();

  static {
    DEFAULT_CONTENT_SECURITY_POLICY_DIRECTIVE.put(
      "default-src",
      Arrays.asList(SELF)
    );
    DEFAULT_CONTENT_SECURITY_POLICY_DIRECTIVE.put(
      "base-uri",
      Arrays.asList(SELF)
    );
    DEFAULT_CONTENT_SECURITY_POLICY_DIRECTIVE.put(
      "font-src",
      Arrays.asList(SELF, "https:", "data:")
    );
    DEFAULT_CONTENT_SECURITY_POLICY_DIRECTIVE.put(
      "form-action",
      Arrays.asList(SELF)
    );
    DEFAULT_CONTENT_SECURITY_POLICY_DIRECTIVE.put(
      "frame-ancestors",
      Arrays.asList(SELF)
    );
    DEFAULT_CONTENT_SECURITY_POLICY_DIRECTIVE.put(
      "img-src",
      Arrays.asList(SELF, "data:")
    );
    DEFAULT_CONTENT_SECURITY_POLICY_DIRECTIVE.put(
      "object-src",
      Arrays.asList("'none'")
    );
    DEFAULT_CONTENT_SECURITY_POLICY_DIRECTIVE.put(
      "script-src",
      Arrays.asList(SELF)
    );
    DEFAULT_CONTENT_SECURITY_POLICY_DIRECTIVE.put(
      "script-src-attr",
      Arrays.asList("'none'")
    );
    DEFAULT_CONTENT_SECURITY_POLICY_DIRECTIVE.put(
      "style-src",
      Arrays.asList(SELF, "https:", "'unsafe-inline'")
    );
    DEFAULT_CONTENT_SECURITY_POLICY_DIRECTIVE.put(
      "upgrade-insecure-requests",
      Arrays.asList()
    );
  }

  private ApiSecurityConstants() {}
}
