package com.sourcefuse.jarc.authlib.api.security.header.utils;

import com.sourcefuse.jarc.authlib.api.security.config.ReferrerPolicyConfigOptions;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;

public final class ReferrerPolicyHeader {

  private ReferrerPolicyHeader() {}

  public static ReferrerPolicy getHeaderValue(
    ReferrerPolicyConfigOptions options
  ) {
    if (options == null || options.getPolicy() == null) {
      return ReferrerPolicy.NO_REFERRER;
    }
    return options.getPolicy();
  }
}
