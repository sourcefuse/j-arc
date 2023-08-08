package com.sourcefuse.jarc.authlib.api.security.types;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ApiSecurityConstants {

  public static final String SELF = "'self'";

  // 180 * 24 * 60 * 60 = 15552000
  public static final Double DEFAULT_MAX_AGE = 15552000D;

  @SuppressWarnings("unchecked")
  public static final Map<String, Object> DEFAULT_CONTENT_SECURITY_POLICY_DIRECTIVE =
    Stream
      .of(
        new Object[][] {
          { "default-src", Arrays.asList(SELF) },
          { "base-uri", Arrays.asList(SELF) },
          { "font-src", Arrays.asList(SELF, "https:", "data:") },
          { "form-action", Arrays.asList(SELF) },
          { "frame-ancestors", Arrays.asList(SELF) },
          { "img-src", Arrays.asList(SELF, "data:") },
          { "object-src", Arrays.asList("'none'") },
          { "script-src", Arrays.asList(SELF) },
          { "script-src-attr", Arrays.asList("'none'") },
          { "style-src", Arrays.asList(SELF, "https:", "'unsafe-inline'") },
          { "upgrade-insecure-requests", Arrays.asList() }
        }
      )
      .collect(
        Collectors.toMap(
          data -> (String) data[0],
          data -> (List<String>) data[1]
        )
      );

  private ApiSecurityConstants() {}
}
