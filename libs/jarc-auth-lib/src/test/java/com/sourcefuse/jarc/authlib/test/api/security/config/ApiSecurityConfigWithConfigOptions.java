package com.sourcefuse.jarc.authlib.test.api.security.config;

import com.sourcefuse.jarc.authlib.api.security.config.ApiSecurityConfig;
import com.sourcefuse.jarc.authlib.api.security.config.ContentSecurityPolicyConfigOptions;
import com.sourcefuse.jarc.authlib.api.security.config.CrossOriginEmbedderPolicyConfigOptions;
import com.sourcefuse.jarc.authlib.api.security.config.CrossOriginOpenerPolicyConfigOptions;
import com.sourcefuse.jarc.authlib.api.security.config.CrossOriginResourcePolicyConfigOptions;
import com.sourcefuse.jarc.authlib.api.security.config.ReferrerPolicyConfigOptions;
import com.sourcefuse.jarc.authlib.api.security.config.StrictTransportSecurityConfigOptions;
import com.sourcefuse.jarc.authlib.api.security.config.XDnsPrefetchControlConfigOptions;
import com.sourcefuse.jarc.authlib.api.security.config.XFrameOptionsConfigOptions;
import com.sourcefuse.jarc.authlib.api.security.config.XPermittedCrossDomainPoliciesConfigOptions;
import com.sourcefuse.jarc.authlib.api.security.header.utils.ContentSecurityPolicyHeader;
import com.sourcefuse.jarc.authlib.api.security.header.writers.XPermittedCrossDomainPoliciesHeaderWriter;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.security.web.header.writers.CrossOriginEmbedderPolicyHeaderWriter.CrossOriginEmbedderPolicy;
import org.springframework.security.web.header.writers.CrossOriginOpenerPolicyHeaderWriter.CrossOriginOpenerPolicy;
import org.springframework.security.web.header.writers.CrossOriginResourcePolicyHeaderWriter.CrossOriginResourcePolicy;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

public class ApiSecurityConfigWithConfigOptions implements ApiSecurityConfig {

  @Override
  public Object enableContentSecurityPolicy() {
    return new ContentSecurityPolicyConfigOptions(
      false,
      Stream
        .of(
          new Object[][] {
            {
              "default-src",
              ContentSecurityPolicyHeader.DANGEROUSLY_DISABLE_DEFAULT_SRC
            },
            { "object-src", Arrays.asList("'none'") },
            { "script-src", Arrays.asList("'self'", "js.example.com") },
            { "upgrade-insecure-requests", Arrays.asList() }
          }
        )
        .collect(
          Collectors.toMap(data -> (String) data[0], data -> (Object) data[1])
        ),
      false
    );
  }

  @Override
  public Object enableCrossOriginEmbedderPolicy() {
    return new CrossOriginEmbedderPolicyConfigOptions(
      CrossOriginEmbedderPolicy.UNSAFE_NONE
    );
  }

  @Override
  public Object enableCrossOriginOpenerPolicy() {
    return new CrossOriginOpenerPolicyConfigOptions(
      CrossOriginOpenerPolicy.SAME_ORIGIN_ALLOW_POPUPS
    );
  }

  @Override
  public Object enableCrossOriginResourcePolicy() {
    return new CrossOriginResourcePolicyConfigOptions(
      CrossOriginResourcePolicy.SAME_SITE
    );
  }

  @Override
  public Boolean enableOriginAgentCluster() {
    return true;
  }

  @Override
  public Object enableReferrerPolicy() {
    return new ReferrerPolicyConfigOptions(
      ReferrerPolicyHeaderWriter.ReferrerPolicy.ORIGIN_WHEN_CROSS_ORIGIN
    );
  }

  @Override
  public Object enableStrictTransportSecurity() {
    return new StrictTransportSecurityConfigOptions(180000D, true, true);
  }

  @Override
  public Boolean enableXContentTypeOptions() {
    return true;
  }

  @Override
  public Object enableXDnsPrefetchControl() {
    return new XDnsPrefetchControlConfigOptions(true);
  }

  @Override
  public Boolean enableXDownloadOptions() {
    return true;
  }

  @Override
  public Object enableXFrameOptions() {
    return new XFrameOptionsConfigOptions("sameorigin");
  }

  @Override
  public Object enableXPermittedCrossDomainPolicies() {
    return new XPermittedCrossDomainPoliciesConfigOptions(
      XPermittedCrossDomainPoliciesHeaderWriter.PermittedPolicy.ALL
    );
  }

  @Override
  public Boolean enableXXssProtection() {
    return true;
  }
}
