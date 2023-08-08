package com.sourcefuse.jarc.authlib.api.security.config;

public interface ApiSecurityConfig {
  /**
   * Either return Boolean or ContentSecurityPolicyConfig object
   *
   * @return Boolean or ContentSecurityPolicyConfig
   */
  Object enableContentSecurityPolicy();

  /**
   * Either return Boolean or CrossOriginEmbedderPolicyConfig object
   *
   * @return Boolean or CrossOriginEmbedderPolicyConfig
   */
  Object enableCrossOriginEmbedderPolicy();

  /**
   * Either return Boolean or CrossOriginOpenerPolicyConfig object
   *
   * @return Boolean or CrossOriginOpenerPolicyConfig
   */
  Object enableCrossOriginOpenerPolicy();

  /**
   * Either return Boolean or CrossOriginResourcePolicyConfig object
   *
   * @return Boolean or CrossOriginResourcePolicyConfig
   */
  Object enableCrossOriginResourcePolicy();

  Boolean enableOriginAgentCluster();

  /**
   * Either return Boolean or ReferrerPolicyConfig object
   *
   * @return Boolean or ReferrerPolicyConfig
   */
  Object enableReferrerPolicy();

  /**
   * Either return Boolean or StrictTransportSecurityConfig object
   *
   * @return Boolean or StrictTransportSecurityConfig
   */
  Object enableStrictTransportSecurity();

  Boolean enableXContentTypeOptions();

  /**
   * Either return Boolean or XDnsPrefetchControlConfig object
   *
   * @return Boolean or XDnsPrefetchControlConfig
   */
  Object enableXDnsPrefetchControl();

  Boolean enableXDownloadOptions();

  /**
   * Either return Boolean or XFrameOptionsConfig object
   *
   * @return Boolean or XFrameOptionsConfig
   */
  Object enableXFrameOptions();

  /**
   * Either return Boolean or XPermittedCrossDomainPoliciesConfig object
   *
   * @return Boolean or XPermittedCrossDomainPoliciesConfig
   */
  Object enableXPermittedCrossDomainPolicies();

  Boolean enableXPoweredBy();

  Boolean enableXXssProtection();
}
