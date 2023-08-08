package com.sourcefuse.jarc.authlib.api.security.header.utils;

import com.sourcefuse.jarc.authlib.api.security.config.CrossOriginEmbedderPolicyConfig;
import org.springframework.security.web.header.writers.CrossOriginEmbedderPolicyHeaderWriter.CrossOriginEmbedderPolicy;

public final class CrossOriginEmbedderPolicyHeader {

  public static CrossOriginEmbedderPolicy getHeaderValue(
    CrossOriginEmbedderPolicyConfig options
  ) {
    if (options == null || options.getPolicy() == null) {
      return CrossOriginEmbedderPolicy.REQUIRE_CORP;
    }
    return options.getPolicy();
  }
}
