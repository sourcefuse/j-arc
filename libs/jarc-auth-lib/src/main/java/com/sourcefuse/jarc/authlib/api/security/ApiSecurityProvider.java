package com.sourcefuse.jarc.authlib.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.sourcefuse.jarc.authlib.api.security.header.utils.CrossOriginEmbedderPolicyHeader;
import com.sourcefuse.jarc.authlib.api.security.header.utils.CrossOriginOpenerPolicyHeader;
import com.sourcefuse.jarc.authlib.api.security.header.utils.CrossOriginResourcePolicyHeader;
import com.sourcefuse.jarc.authlib.api.security.header.utils.ReferrerPolicyHeader;
import com.sourcefuse.jarc.authlib.api.security.header.utils.StrictTransportSecurityHeader;
import com.sourcefuse.jarc.authlib.api.security.header.utils.XFrameOptionsHeader;
import com.sourcefuse.jarc.authlib.api.security.header.writers.XDnsPrefetchControlHeaderWriter;
import com.sourcefuse.jarc.authlib.api.security.header.writers.XPermittedCrossDomainPoliciesHeaderWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnBean(ApiSecurityConfig.class)
public class ApiSecurityProvider {

  private final ApiSecurityConfig apiSecurityConfig;

  private ObjectMapper objectMapper = new ObjectMapper();

  public void setApiSecurity(HttpSecurity http) throws Exception {
    processContentSecurityPolicy(http);
    processCrossOriginEmbedderPolicy(http);
    processCrossOriginOpenerPolicy(http);
    processCrossOriginResourcePolicy(http);
    processOriginAgentCluster(http);
    processReferrerPolicy(http);
    processStrictTransportSecurity(http);
    processXContentTypeOptions(http);
    processXDnsPrefetchControl(http);
    processXDownloadOptions(http);
    processXFrameOptions(http);
    processXPermittedCrossDomainPolicies(http);
    processXXssProtections(http);
  }

  public void processContentSecurityPolicy(HttpSecurity http) throws Exception {
    if (apiSecurityConfig.enableContentSecurityPolicy() instanceof Boolean) {
      boolean optionValue =
        (boolean) apiSecurityConfig.enableContentSecurityPolicy();
      if (optionValue) {
        http
          .headers()
          .contentSecurityPolicy(
            ContentSecurityPolicyHeader.getHeaderValue(null)
          );
      }
    } else {
      ContentSecurityPolicyConfigOptions options = objectMapper.convertValue(
        apiSecurityConfig.enableContentSecurityPolicy(),
        ContentSecurityPolicyConfigOptions.class
      );
      if (options != null && options.isReportOnly()) {
        http
          .headers()
          .contentSecurityPolicy(
            ContentSecurityPolicyHeader.getHeaderValue(options)
          )
          .reportOnly();
      } else {
        http
          .headers()
          .contentSecurityPolicy(
            ContentSecurityPolicyHeader.getHeaderValue(options)
          );
      }
    }
  }

  public void processCrossOriginEmbedderPolicy(HttpSecurity http)
    throws Exception {
    if (
      apiSecurityConfig.enableCrossOriginEmbedderPolicy() instanceof Boolean
    ) {
      boolean optionValue =
        (boolean) apiSecurityConfig.enableCrossOriginEmbedderPolicy();
      if (optionValue) {
        http
          .headers()
          .crossOriginEmbedderPolicy()
          .policy(CrossOriginEmbedderPolicyHeader.getHeaderValue(null));
      }
    } else {
      CrossOriginEmbedderPolicyConfigOptions options =
        objectMapper.convertValue(
          apiSecurityConfig.enableCrossOriginEmbedderPolicy(),
          CrossOriginEmbedderPolicyConfigOptions.class
        );
      http
        .headers()
        .crossOriginEmbedderPolicy()
        .policy(CrossOriginEmbedderPolicyHeader.getHeaderValue(options));
    }
  }

  public void processCrossOriginOpenerPolicy(HttpSecurity http)
    throws Exception {
    if (apiSecurityConfig.enableCrossOriginOpenerPolicy() instanceof Boolean) {
      boolean optionValue =
        (boolean) apiSecurityConfig.enableCrossOriginOpenerPolicy();
      if (optionValue) {
        http
          .headers()
          .crossOriginOpenerPolicy()
          .policy(CrossOriginOpenerPolicyHeader.getHeaderValue(null));
      }
    } else {
      CrossOriginOpenerPolicyConfigOptions options = objectMapper.convertValue(
        apiSecurityConfig.enableCrossOriginOpenerPolicy(),
        CrossOriginOpenerPolicyConfigOptions.class
      );
      http
        .headers()
        .crossOriginOpenerPolicy()
        .policy(CrossOriginOpenerPolicyHeader.getHeaderValue(options));
    }
  }

  public void processCrossOriginResourcePolicy(HttpSecurity http)
    throws Exception {
    if (
      apiSecurityConfig.enableCrossOriginResourcePolicy() instanceof Boolean
    ) {
      boolean optionValue =
        (boolean) apiSecurityConfig.enableCrossOriginResourcePolicy();
      if (optionValue) {
        http
          .headers()
          .crossOriginResourcePolicy()
          .policy(CrossOriginResourcePolicyHeader.getHeaderValue(null));
      }
    } else {
      CrossOriginResourcePolicyConfigOptions options =
        objectMapper.convertValue(
          apiSecurityConfig.enableCrossOriginResourcePolicy(),
          CrossOriginResourcePolicyConfigOptions.class
        );
      http
        .headers()
        .crossOriginResourcePolicy()
        .policy(CrossOriginResourcePolicyHeader.getHeaderValue(options));
    }
  }

  public void processOriginAgentCluster(HttpSecurity http) throws Exception {
    if (
      apiSecurityConfig.enableOriginAgentCluster() == null ||
      apiSecurityConfig.enableOriginAgentCluster()
    ) {
      http
        .headers()
        .addHeaderWriter(new StaticHeadersWriter("Origin-Agent-Cluster", "?1"));
    }
  }

  public void processReferrerPolicy(HttpSecurity http) throws Exception {
    if (apiSecurityConfig.enableReferrerPolicy() instanceof Boolean) {
      boolean optionValue = (boolean) apiSecurityConfig.enableReferrerPolicy();
      if (optionValue) {
        http
          .headers()
          .referrerPolicy()
          .policy(ReferrerPolicyHeader.getHeaderValue(null));
      }
    } else {
      ReferrerPolicyConfigOptions options = objectMapper.convertValue(
        apiSecurityConfig.enableReferrerPolicy(),
        ReferrerPolicyConfigOptions.class
      );
      http
        .headers()
        .referrerPolicy()
        .policy(ReferrerPolicyHeader.getHeaderValue(options));
    }
  }

  public void processStrictTransportSecurity(HttpSecurity http)
    throws Exception {
    if (apiSecurityConfig.enableStrictTransportSecurity() instanceof Boolean) {
      boolean optionValue =
        (boolean) apiSecurityConfig.enableStrictTransportSecurity();
      if (optionValue) {
        StrictTransportSecurityHeader.setHeaderByConfig(
          http.headers().httpStrictTransportSecurity(),
          null
        );
      } else {
        http.headers().httpStrictTransportSecurity().disable();
      }
    } else {
      StrictTransportSecurityConfigOptions options = objectMapper.convertValue(
        apiSecurityConfig.enableStrictTransportSecurity(),
        StrictTransportSecurityConfigOptions.class
      );

      StrictTransportSecurityHeader.setHeaderByConfig(
        http.headers().httpStrictTransportSecurity(),
        options
      );
    }
  }

  public void processXContentTypeOptions(HttpSecurity http) throws Exception {
    // this is for disabling default spring security behaviour
    if (Boolean.FALSE.equals(apiSecurityConfig.enableXContentTypeOptions())) {
      http.headers().contentTypeOptions().disable();
    }
  }

  public void processXDnsPrefetchControl(HttpSecurity http) throws Exception {
    if (apiSecurityConfig.enableXDnsPrefetchControl() instanceof Boolean) {
      boolean optionValue =
        (boolean) apiSecurityConfig.enableXDnsPrefetchControl();
      if (optionValue) {
        http.headers().addHeaderWriter(new XDnsPrefetchControlHeaderWriter());
      }
    } else {
      XDnsPrefetchControlConfigOptions options = objectMapper.convertValue(
        apiSecurityConfig.enableXDnsPrefetchControl(),
        XDnsPrefetchControlConfigOptions.class
      );
      http
        .headers()
        .addHeaderWriter(new XDnsPrefetchControlHeaderWriter(options));
    }
  }

  public void processXDownloadOptions(HttpSecurity http) throws Exception {
    if (
      apiSecurityConfig.enableXDownloadOptions() == null ||
      apiSecurityConfig.enableXDownloadOptions()
    ) {
      http
        .headers()
        .addHeaderWriter(
          new StaticHeadersWriter("X-Download-Options", "noopen")
        );
    }
  }

  public void processXFrameOptions(HttpSecurity http) throws Exception {
    if (apiSecurityConfig.enableXFrameOptions() instanceof Boolean) {
      boolean optionValue = (boolean) apiSecurityConfig.enableXFrameOptions();
      if (optionValue) {
        XFrameOptionsHeader.setValue(http, null);
      } else {
        http.headers().frameOptions().disable();
      }
    } else {
      XFrameOptionsConfigOptions options = objectMapper.convertValue(
        apiSecurityConfig.enableXFrameOptions(),
        XFrameOptionsConfigOptions.class
      );
      XFrameOptionsHeader.setValue(http, options);
    }
  }

  public void processXPermittedCrossDomainPolicies(HttpSecurity http)
    throws Exception {
    if (
      apiSecurityConfig.enableXPermittedCrossDomainPolicies() instanceof Boolean
    ) {
      boolean optionValue =
        (boolean) apiSecurityConfig.enableXPermittedCrossDomainPolicies();
      if (optionValue) {
        http
          .headers()
          .addHeaderWriter(new XPermittedCrossDomainPoliciesHeaderWriter());
      }
    } else {
      XPermittedCrossDomainPoliciesConfigOptions options =
        objectMapper.convertValue(
          apiSecurityConfig.enableXPermittedCrossDomainPolicies(),
          XPermittedCrossDomainPoliciesConfigOptions.class
        );
      http
        .headers()
        .addHeaderWriter(
          new XPermittedCrossDomainPoliciesHeaderWriter(options)
        );
    }
  }

  public void processXXssProtections(HttpSecurity http) throws Exception {
    // this is for disabling default spring security behaviour
    if (
      apiSecurityConfig.enableXXssProtection() == null ||
      apiSecurityConfig.enableXXssProtection()
    ) {
      http
        .headers()
        .xssProtection()
        .headerValue(XXssProtectionHeaderWriter.HeaderValue.DISABLED);
    } else {
      http.headers().xssProtection().disable();
    }
  }
}
