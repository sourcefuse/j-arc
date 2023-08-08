package com.sourcefuse.jarc.authlib.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcefuse.jarc.authlib.api.security.config.ApiSecurityConfig;
import com.sourcefuse.jarc.authlib.api.security.config.ContentSecurityPolicyConfig;
import com.sourcefuse.jarc.authlib.api.security.config.CrossOriginEmbedderPolicyConfig;
import com.sourcefuse.jarc.authlib.api.security.config.CrossOriginOpenerPolicyConfig;
import com.sourcefuse.jarc.authlib.api.security.config.CrossOriginResourcePolicyConfig;
import com.sourcefuse.jarc.authlib.api.security.config.ReferrerPolicyConfig;
import com.sourcefuse.jarc.authlib.api.security.config.StrictTransportSecurityConfig;
import com.sourcefuse.jarc.authlib.api.security.config.XDnsPrefetchControlConfig;
import com.sourcefuse.jarc.authlib.api.security.config.XFrameOptionsConfig;
import com.sourcefuse.jarc.authlib.api.security.config.XPermittedCrossDomainPoliciesConfig;
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
      ContentSecurityPolicyConfig options = objectMapper.convertValue(
        apiSecurityConfig.enableContentSecurityPolicy(),
        ContentSecurityPolicyConfig.class
      );
      if (options.isReportOnly()) {
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
          .crossOriginEmbedderPolicy(config -> {
            config.policy(CrossOriginEmbedderPolicyHeader.getHeaderValue(null));
          });
      }
    } else {
      CrossOriginEmbedderPolicyConfig options = objectMapper.convertValue(
        apiSecurityConfig.enableCrossOriginEmbedderPolicy(),
        CrossOriginEmbedderPolicyConfig.class
      );
      http
        .headers()
        .crossOriginEmbedderPolicy(config -> {
          config.policy(
            CrossOriginEmbedderPolicyHeader.getHeaderValue(options)
          );
        });
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
          .crossOriginOpenerPolicy(config -> {
            config.policy(CrossOriginOpenerPolicyHeader.getHeaderValue(null));
          });
      }
    } else {
      CrossOriginOpenerPolicyConfig options = objectMapper.convertValue(
        apiSecurityConfig.enableCrossOriginOpenerPolicy(),
        CrossOriginOpenerPolicyConfig.class
      );
      http
        .headers()
        .crossOriginOpenerPolicy(config -> {
          config.policy(CrossOriginOpenerPolicyHeader.getHeaderValue(options));
        });
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
          .crossOriginResourcePolicy(config -> {
            config.policy(CrossOriginResourcePolicyHeader.getHeaderValue(null));
          });
      }
    } else {
      CrossOriginResourcePolicyConfig options = objectMapper.convertValue(
        apiSecurityConfig.enableCrossOriginResourcePolicy(),
        CrossOriginResourcePolicyConfig.class
      );
      http
        .headers()
        .crossOriginResourcePolicy(config -> {
          config.policy(
            CrossOriginResourcePolicyHeader.getHeaderValue(options)
          );
        });
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
          .referrerPolicy(referrerConfig -> {
            referrerConfig.policy(ReferrerPolicyHeader.getHeaderValue(null));
          });
      }
    } else {
      ReferrerPolicyConfig options = objectMapper.convertValue(
        apiSecurityConfig.enableReferrerPolicy(),
        ReferrerPolicyConfig.class
      );
      http
        .headers()
        .referrerPolicy(referrerConfig -> {
          referrerConfig.policy(ReferrerPolicyHeader.getHeaderValue(options));
        });
    }
  }

  public void processStrictTransportSecurity(HttpSecurity http)
    throws Exception {
    if (apiSecurityConfig.enableStrictTransportSecurity() instanceof Boolean) {
      boolean optionValue =
        (boolean) apiSecurityConfig.enableStrictTransportSecurity();
      if (optionValue) {
        http
          .headers()
          .httpStrictTransportSecurity(hstsConfig -> {
            StrictTransportSecurityHeader.setHeaderByConfig(hstsConfig, null);
          });
      } else {
        http.headers().httpStrictTransportSecurity().disable();
      }
    } else {
      StrictTransportSecurityConfig options = objectMapper.convertValue(
        apiSecurityConfig.enableStrictTransportSecurity(),
        StrictTransportSecurityConfig.class
      );

      http
        .headers()
        .httpStrictTransportSecurity(hstsConfig -> {
          StrictTransportSecurityHeader.setHeaderByConfig(hstsConfig, options);
        });
    }
  }

  public void processXContentTypeOptions(HttpSecurity http) throws Exception {
    // this is for disabling default spring security behaviour
    if (!apiSecurityConfig.enableXContentTypeOptions()) {
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
      XDnsPrefetchControlConfig options = objectMapper.convertValue(
        apiSecurityConfig.enableXDnsPrefetchControl(),
        XDnsPrefetchControlConfig.class
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
      XFrameOptionsConfig options = objectMapper.convertValue(
        apiSecurityConfig.enableXFrameOptions(),
        XFrameOptionsConfig.class
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
      XPermittedCrossDomainPoliciesConfig options = objectMapper.convertValue(
        apiSecurityConfig.enableXPermittedCrossDomainPolicies(),
        XPermittedCrossDomainPoliciesConfig.class
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
