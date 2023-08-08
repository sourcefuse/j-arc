package com.sourcefuse.jarc.authlib.api.security.header.utils;

import com.sourcefuse.jarc.authlib.api.security.config.ReferrerPolicyConfig;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;

public class ReferrerPolicyHeader {

  public static ReferrerPolicy getHeaderValue(ReferrerPolicyConfig options) {
    if (options == null || options.getPolicy() == null) {
      return ReferrerPolicy.NO_REFERRER;
    }
    return options.getPolicy();
  }
}
