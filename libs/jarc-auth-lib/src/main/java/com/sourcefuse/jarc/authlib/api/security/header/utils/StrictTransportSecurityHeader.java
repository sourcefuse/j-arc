package com.sourcefuse.jarc.authlib.api.security.header.utils;

import com.sourcefuse.jarc.authlib.api.security.config.StrictTransportSecurityConfigOptions;
import com.sourcefuse.jarc.authlib.api.security.types.ApiSecurityConstants;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.HstsConfig;

public final class StrictTransportSecurityHeader {

  private StrictTransportSecurityHeader() {}

  private static Long parseMaxAge(Double maxAge) {
    if (maxAge == null) {
      maxAge = ApiSecurityConstants.DEFAULT_MAX_AGE;
    }
    if (maxAge >= 0 && Double.isFinite(maxAge)) {
      return (long) Math.floor(maxAge);
    } else {
      throw new IllegalArgumentException(
        "Strict-Transport-Security: " +
        maxAge +
        " is not a valid value for maxAge. Please choose a positive integer."
      );
    }
  }

  @SuppressWarnings("rawtypes")
  public static HstsConfig setHeaderByConfig(
    HstsConfig hstsConfig,
    StrictTransportSecurityConfigOptions options
  ) {
    if (options == null) {
      options = new StrictTransportSecurityConfigOptions();
    }
    hstsConfig.maxAgeInSeconds(parseMaxAge(options.getMaxAge()));

    if (
      options.getIncludeSubDomains() == null || options.getIncludeSubDomains()
    ) {
      hstsConfig.includeSubDomains(true);
    }

    if (options.isPreload()) {
      hstsConfig.preload(true);
    }
    return hstsConfig;
  }
}
