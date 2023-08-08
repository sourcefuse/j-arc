package com.sourcefuse.jarc.authlib.api.security.config;

public interface ApiSecurityConfig {
  /**
   * Either return Boolean or ContentSecurityPolicyConfigOptions object
   *
   * @return Boolean or ContentSecurityPolicyConfigOptions
   */
  Object enableContentSecurityPolicy();

  /**
   * Either return Boolean or CrossOriginEmbedderPolicyConfigOptions object
   *
   * @return Boolean or CrossOriginEmbedderPolicyConfigOptions
   */
  Object enableCrossOriginEmbedderPolicy();

  /**
   * Either return Boolean or CrossOriginOpenerPolicyConfigOptions object
   *
   * @return Boolean or CrossOriginOpenerPolicyConfigOptions
   */
  Object enableCrossOriginOpenerPolicy();

  /**
   * Either return Boolean or CrossOriginResourcePolicyConfigOptions object
   *
   * @return Boolean or CrossOriginResourcePolicyConfigOptions
   */
  Object enableCrossOriginResourcePolicy();

  Boolean enableOriginAgentCluster();

  /**
   * Either return Boolean or ReferrerPolicyConfigOptions object
   *
   * @return Boolean or ReferrerPolicyConfigOptions
   */
  Object enableReferrerPolicy();

  /**
   * Either return Boolean or StrictTransportSecurityConfigOptions object
   *
   * @return Boolean or StrictTransportSecurityConfigOptions
   */
  Object enableStrictTransportSecurity();

  Boolean enableXContentTypeOptions();

  /**
   * Either return Boolean or XDnsPrefetchControlConfigOptions object
   *
   * @return Boolean or XDnsPrefetchControlConfigOptions
   */
  Object enableXDnsPrefetchControl();

  Boolean enableXDownloadOptions();

  /**
   * Either return Boolean or XFrameOptionsConfigOptions object
   *
   * @return Boolean or XFrameOptionsConfigOptions
   */
  Object enableXFrameOptions();

  /**
   * Either return Boolean or XPermittedCrossDomainPoliciesConfigOptions object
   *
   * @return Boolean or XPermittedCrossDomainPoliciesConfigOptions
   */
  Object enableXPermittedCrossDomainPolicies();

  Boolean enableXPoweredBy();

  Boolean enableXXssProtection();
}
