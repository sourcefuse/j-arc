package com.sourcefuse.jarc.authlib.test.api.security.config;

import com.sourcefuse.jarc.authlib.api.security.config.ApiSecurityConfig;

public class DisabledApiSecurityConfig implements ApiSecurityConfig {

  @Override
  public Object enableContentSecurityPolicy() {
    return false;
  }

  @Override
  public Object enableCrossOriginEmbedderPolicy() {
    return false;
  }

  @Override
  public Object enableCrossOriginOpenerPolicy() {
    return false;
  }

  @Override
  public Object enableCrossOriginResourcePolicy() {
    return false;
  }

  @Override
  public Boolean enableOriginAgentCluster() {
    return false;
  }

  @Override
  public Object enableReferrerPolicy() {
    return false;
  }

  @Override
  public Object enableStrictTransportSecurity() {
    return false;
  }

  @Override
  public Boolean enableXContentTypeOptions() {
    return false;
  }

  @Override
  public Object enableXDnsPrefetchControl() {
    return false;
  }

  @Override
  public Boolean enableXDownloadOptions() {
    return false;
  }

  @Override
  public Object enableXFrameOptions() {
    return false;
  }

  @Override
  public Object enableXPermittedCrossDomainPolicies() {
    return false;
  }

  @Override
  public Boolean enableXXssProtection() {
    return false;
  }
}
