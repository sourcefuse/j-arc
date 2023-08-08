package com.sourcefuse.jarc.authlib.api.security.header.utils;

import com.sourcefuse.jarc.authlib.api.security.config.CrossOriginResourcePolicyConfig;
import org.springframework.security.web.header.writers.CrossOriginResourcePolicyHeaderWriter.CrossOriginResourcePolicy;

public final class CrossOriginResourcePolicyHeader {

  public static CrossOriginResourcePolicy getHeaderValue(
    CrossOriginResourcePolicyConfig options
  ) {
    if (options == null || options.getPolicy() == null) {
      return CrossOriginResourcePolicy.SAME_ORIGIN;
    }
    return options.getPolicy();
  }
}
