package com.sourcefuse.jarc.authlib.test.api.security.config;

import com.sourcefuse.jarc.authlib.api.security.config.ApiSecurityConfig;

public class NullApiSecurityConfig implements ApiSecurityConfig {

  @Override
  public Object enableContentSecurityPolicy() {
    return null;
  }

  @Override
  public Object enableCrossOriginEmbedderPolicy() {
    return null;
  }

  @Override
  public Object enableCrossOriginOpenerPolicy() {
    return null;
  }

  @Override
  public Object enableCrossOriginResourcePolicy() {
    return null;
  }

  @Override
  public Boolean enableOriginAgentCluster() {
    return null;
  }

  @Override
  public Object enableReferrerPolicy() {
    return null;
  }

  @Override
  public Object enableStrictTransportSecurity() {
    return null;
  }

  @Override
  public Boolean enableXContentTypeOptions() {
    return null;
  }

  @Override
  public Object enableXDnsPrefetchControl() {
    return null;
  }

  @Override
  public Boolean enableXDownloadOptions() {
    return null;
  }

  @Override
  public Object enableXFrameOptions() {
    return null;
  }

  @Override
  public Object enableXPermittedCrossDomainPolicies() {
    return null;
  }

  @Override
  public Boolean enableXXssProtection() {
    return null;
  }
}
