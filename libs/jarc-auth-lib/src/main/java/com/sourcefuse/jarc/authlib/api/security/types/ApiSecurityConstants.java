package com.sourcefuse.jarc.authlib.api.security.types;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ApiSecurityConstants {

  public static final String SELF = "'self'";

  // 180 * 24 * 60 * 60 = 15552000
  public static final Double DEFAULT_MAX_AGE = 15552000D;

  public static final Map<String, Object> DEFAULT_CONTENT_SECURITY_POLICY_DIRECTIVE;

  static {
    final Map<String, Object> tmpMap = new HashMap<>();
    tmpMap.put("default-src", Arrays.asList(SELF));
    tmpMap.put("base-uri", Arrays.asList(SELF));
    tmpMap.put("font-src", Arrays.asList(SELF, "https:", "data:"));
    tmpMap.put("form-action", Arrays.asList(SELF));
    tmpMap.put("frame-ancestors", Arrays.asList(SELF));
    tmpMap.put("img-src", Arrays.asList(SELF, "data:"));
    tmpMap.put("object-src", Arrays.asList("'none'"));
    tmpMap.put("script-src", Arrays.asList(SELF));
    tmpMap.put("script-src-attr", Arrays.asList("'none'"));
    tmpMap.put("style-src", Arrays.asList(SELF, "https:", "'unsafe-inline'"));
    tmpMap.put("upgrade-insecure-requests", Arrays.asList());
    DEFAULT_CONTENT_SECURITY_POLICY_DIRECTIVE =
      Collections.unmodifiableMap(tmpMap);
  }

  private ApiSecurityConstants() {}
}
