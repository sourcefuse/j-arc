package com.sourcefuse.jarc.authlib.test.api.security.config;

import com.sourcefuse.jarc.authlib.api.security.config.ApiSecurityConfig;

public class EnabledApiSecurityConfig implements ApiSecurityConfig {

  @Override
  public Object enableContentSecurityPolicy() {
    return true;
  }

  @Override
  public Object enableCrossOriginEmbedderPolicy() {
    return true;
  }

  @Override
  public Object enableCrossOriginOpenerPolicy() {
    return true;
  }

  @Override
  public Object enableCrossOriginResourcePolicy() {
    return true;
  }

  @Override
  public Boolean enableOriginAgentCluster() {
    return true;
  }

  @Override
  public Object enableReferrerPolicy() {
    return true;
  }

  @Override
  public Object enableStrictTransportSecurity() {
    return true;
  }

  @Override
  public Boolean enableXContentTypeOptions() {
    return true;
  }

  @Override
  public Object enableXDnsPrefetchControl() {
    return true;
  }

  @Override
  public Boolean enableXDownloadOptions() {
    return true;
  }

  @Override
  public Object enableXFrameOptions() {
    return true;
  }

  @Override
  public Object enableXPermittedCrossDomainPolicies() {
    return true;
  }

  @Override
  public Boolean enableXXssProtection() {
    return true;
  }
}
