package com.sourcefuse.jarc.authlib.api.security;

import com.sourcefuse.jarc.authlib.api.security.config.ApiSecurityConfig;
import com.sourcefuse.jarc.authlib.api.security.config.ContentSecurityPolicyConfigOptions;
import com.sourcefuse.jarc.authlib.api.security.config.ReferrerPolicyConfigOptions;
import com.sourcefuse.jarc.authlib.api.security.config.StrictTransportSecurityConfigOptions;
import com.sourcefuse.jarc.authlib.api.security.config.XDnsPrefetchControlConfigOptions;
import com.sourcefuse.jarc.authlib.api.security.config.XFrameOptionsConfigOptions;
import com.sourcefuse.jarc.authlib.api.security.config.XPermittedCrossDomainPoliciesConfigOptions;
import com.sourcefuse.jarc.authlib.api.security.header.writers.XPermittedCrossDomainPoliciesHeaderWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.stereotype.Service;

@Service
public class Tp implements ApiSecurityConfig {

  @Override
  public Object enableContentSecurityPolicy() {
    // TODO Auto-generated method stub
    //		return null;
    return new ContentSecurityPolicyConfigOptions(
      false,
      Stream
        .of(
          new Object[][] {
            { "default-src", Arrays.asList("'self'", "default.example") },
            { "object-src", Arrays.asList("'none'") },
            { "script-src", Arrays.asList("'self'", "js.example.com") },
            { "upgrade-insecure-requests", Arrays.asList() }
          }
        )
        .collect(
          Collectors.toMap(
            data -> (String) data[0],
            data -> (List<String>) data[1]
          )
        ),
      true
    );
    //		return false;
  }

  @Override
  public Object enableCrossOriginEmbedderPolicy() {
    // TODO Auto-generated method stub
    return false;
    //		return null;
  }

  @Override
  public Object enableCrossOriginOpenerPolicy() {
    // TODO Auto-generated method stub
    return false;
    //		return null;
  }

  @Override
  public Object enableCrossOriginResourcePolicy() {
    // TODO Auto-generated method stub
    return false;
    //		return null;
  }

  @Override
  public Boolean enableOriginAgentCluster() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Object enableReferrerPolicy() {
    // TODO Auto-generated method stub
    return false;
    //		return new ReferrerPolicyConfig(ReferrerPolicyHeaderWriter.ReferrerPolicy.ORIGIN_WHEN_CROSS_ORIGIN);
  }

  @Override
  public Object enableStrictTransportSecurity() {
    // TODO Auto-generated method stub
    return false;
    //		return new StrictTransportSecurityConfig(180000D, true, true);
  }

  @Override
  public Boolean enableXContentTypeOptions() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Object enableXDnsPrefetchControl() {
    // TODO Auto-generated method stub
    return false;
    //		return new XDnsPrefetchControlConfig(true);
  }

  @Override
  public Boolean enableXDownloadOptions() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Object enableXFrameOptions() {
    // TODO Auto-generated method stub
    return false;
    //		return new XFrameOptionsConfig("sameorigin");
  }

  @Override
  public Object enableXPermittedCrossDomainPolicies() {
    // TODO Auto-generated method stub
    return false;
    //		return new XPermittedCrossDomainPoliciesConfig(XPermittedCrossDomainPoliciesHeaderWriter.PermittedPolicy.ALL);
  }

  @Override
  public Boolean enableXPoweredBy() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Boolean enableXXssProtection() {
    // TODO Auto-generated method stub
    return false;
  }
}
