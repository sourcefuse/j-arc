package com.sourcefuse.jarc.authlib.api.security.header.utils;

import com.sourcefuse.jarc.authlib.api.security.config.XFrameOptionsConfigOptions;
import java.util.Set;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public final class XFrameOptionsHeader {

  private static Set<String> ALLOWED_VALUES = Set.of(
    "SAME-ORIGIN",
    "DENY",
    "SAMEORIGIN"
  );

  private XFrameOptionsHeader() {}

  public static void setValue(
    HttpSecurity http,
    XFrameOptionsConfigOptions options
  ) throws Exception {
    String normalizedAction = "sameorigin";
    if (options != null) {
      normalizedAction = options.getAction();
    }
    if (ALLOWED_VALUES.contains(normalizedAction.toUpperCase())) {
      http.headers().frameOptions().sameOrigin();
    } else {
      throw new IllegalArgumentException(
        "X-Frame-Options received an invalid action " + normalizedAction
      );
    }
  }
}
