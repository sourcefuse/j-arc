package com.sourcefuse.jarc.authlib.api.security.header.utils;

import com.sourcefuse.jarc.authlib.api.security.config.CrossOriginResourcePolicyConfigOptions;
import org.springframework.security.web.header.writers.CrossOriginResourcePolicyHeaderWriter.CrossOriginResourcePolicy;

public final class CrossOriginResourcePolicyHeader {

  private CrossOriginResourcePolicyHeader() {}

  public static CrossOriginResourcePolicy getHeaderValue(
    CrossOriginResourcePolicyConfigOptions options
  ) {
    if (options == null || options.getPolicy() == null) {
      return CrossOriginResourcePolicy.SAME_ORIGIN;
    }
    return options.getPolicy();
  }
}
