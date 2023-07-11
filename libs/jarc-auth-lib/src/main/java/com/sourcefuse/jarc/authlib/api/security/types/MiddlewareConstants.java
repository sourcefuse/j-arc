package com.sourcefuse.jarc.authlib.api.security.types;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class MiddlewareConstants {
  public static final String SELF = "'self'";
  public static final String SAME_ORIGIN = "same-origin";

  // 180 * 24 * 60 * 60 = 15552000
  public static final Double DEFAULT_MAX_AGE = 15552000D;

  public static final Set<String> ALLOWED_PERMITTED_CROSS_DOMAIN_POLICIES =
    Set.of("none", "master-only", "by-content-type", "all");

  public static final Set<String> ALLOWED_REFFERER_POLICY_TOKENS = Set.of(
    "no-referrer",
    "no-referrer-when-downgrade",
    SAME_ORIGIN,
    "origin",
    "strict-origin",
    "origin-when-cross-origin",
    "strict-origin-when-cross-origin",
    "unsafe-url",
    ""
  );

  public static final Set<String> ALLOWED_CROSS_ORIGIN_RESOURCE_POLICIES =
    Set.of(SAME_ORIGIN, "same-site", "cross-origin");

  public static final Set<String> ALLOWED_CROSS_ORIGIN_OPENER_POLICIES = Set.of(
    SAME_ORIGIN,
    "same-origin-allow-popups",
    "unsafe-none"
  );

  public static final Set<String> ALLOWED_CROSS_ORIGIN_EMBEDDER_POLICIES =
    Set.of("require-corp", "credentialless");

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

  private MiddlewareConstants() {}
}
