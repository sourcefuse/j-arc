package com.sourcefuse.jarc.authlib.api.security.header.utils;

import com.sourcefuse.jarc.authlib.api.security.config.XFrameOptionsConfig;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public final class XFrameOptionsHeader {

  public static void setValue(HttpSecurity http, XFrameOptionsConfig options)
    throws Exception {
    String normalizedAction = "sameorigin";
    if (options != null) {
      normalizedAction = options.getAction();
    }
    switch (normalizedAction.toUpperCase()) {
      case "SAME-ORIGIN":
      case "DENY":
      case "SAMEORIGIN":
        http.headers().frameOptions().sameOrigin();
        break;
      default:
        throw new IllegalArgumentException(
          "X-Frame-Options received an invalid action " + normalizedAction
        );
    }
  }
}
