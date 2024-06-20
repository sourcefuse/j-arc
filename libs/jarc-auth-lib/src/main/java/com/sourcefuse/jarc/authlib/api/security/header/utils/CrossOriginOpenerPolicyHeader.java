package com.sourcefuse.jarc.authlib.api.security.header.utils;

import com.sourcefuse.jarc.authlib.api.security.config.CrossOriginOpenerPolicyConfigOptions;
import org.springframework.security.web.header.writers.CrossOriginOpenerPolicyHeaderWriter.CrossOriginOpenerPolicy;

public final class CrossOriginOpenerPolicyHeader {

  private CrossOriginOpenerPolicyHeader() {}

  public static CrossOriginOpenerPolicy getHeaderValue(
    CrossOriginOpenerPolicyConfigOptions options
  ) {
    if (options == null || options.getPolicy() == null) {
      return CrossOriginOpenerPolicy.SAME_ORIGIN;
    }
    return options.getPolicy();
  }
}
