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
import com.sourcefuse.jarc.authlib.api.security.middlewares.ContentSecurityPolicy;
import com.sourcefuse.jarc.authlib.api.security.middlewares.CrossOriginEmbedderPolicy;
import com.sourcefuse.jarc.authlib.api.security.middlewares.CrossOriginOpenerPolicy;
import com.sourcefuse.jarc.authlib.api.security.middlewares.CrossOriginResourcePolicy;
import com.sourcefuse.jarc.authlib.api.security.middlewares.OriginAgentCluster;
import com.sourcefuse.jarc.authlib.api.security.middlewares.ReferrerPolicy;
import com.sourcefuse.jarc.authlib.api.security.middlewares.StrictTransportSecurity;
import com.sourcefuse.jarc.authlib.api.security.middlewares.XContentTypeOptions;
import com.sourcefuse.jarc.authlib.api.security.middlewares.XDnsPrefetchControl;
import com.sourcefuse.jarc.authlib.api.security.middlewares.XDownloadOptions;
import com.sourcefuse.jarc.authlib.api.security.middlewares.XFrameOptions;
import com.sourcefuse.jarc.authlib.api.security.middlewares.XPermittedCrossDomainPolicies;
import com.sourcefuse.jarc.authlib.api.security.middlewares.XPoweredBy;
import com.sourcefuse.jarc.authlib.api.security.middlewares.XXssProtection;
import jakarta.servlet.Filter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiSecurityProvider {

  private final ApiSecurityConfig apiSecurityConfig;

  private ObjectMapper objectMapper = new ObjectMapper();

  public HttpSecurity helmet(HttpSecurity http) throws Exception {
    processXPoweredBy(http);

    if (apiSecurityConfig != null) {
      Class<? extends Filter> filterClass = SwitchUserFilter.class;
      for (Filter middleware : getMiddlewaresFromConfig(http)) {
        http.addFilterAfter(middleware, filterClass);
        filterClass = middleware.getClass();
      }
    }
    return http;
  }

  private List<Filter> getMiddlewaresFromConfig(HttpSecurity http)
    throws Exception {
    List<Filter> middlewares = new ArrayList<>();

    processContentSecurityPolicy(middlewares);
    processCrossOriginEmbedderPolicy(middlewares);
    processCrossOriginOpenerPolicy(middlewares);
    processCrossOriginResourcePolicy(middlewares);
    processOriginAgentCluster(middlewares);
    processReferrerPolicy(middlewares);
    processStrictTransportSecurity(middlewares, http);
    processXContentTypeOptions(middlewares, http);
    processXDnsPrefetchControl(middlewares);
    processXDownloadOptions(middlewares);
    processXFrameOptions(middlewares, http);
    processXPermittedCrossDomainPolicies(middlewares);
    processXXssProtections(middlewares, http);

    return middlewares;
  }

  public void processContentSecurityPolicy(List<Filter> middlewares) {
    if (apiSecurityConfig.enableContentSecurityPolicy() instanceof Boolean) {
      boolean optionValue =
        (boolean) apiSecurityConfig.enableContentSecurityPolicy();
      if (optionValue) {
        middlewares.add(new ContentSecurityPolicy());
      }
    } else {
      ContentSecurityPolicyConfig options = objectMapper.convertValue(
        apiSecurityConfig.enableContentSecurityPolicy(),
        ContentSecurityPolicyConfig.class
      );
      middlewares.add(new ContentSecurityPolicy(options));
    }
  }

  public void processCrossOriginEmbedderPolicy(List<Filter> middlewares) {
    if (
      apiSecurityConfig.enableCrossOriginEmbedderPolicy() instanceof Boolean
    ) {
      boolean optionValue =
        (boolean) apiSecurityConfig.enableCrossOriginEmbedderPolicy();
      if (optionValue) {
        middlewares.add(new CrossOriginEmbedderPolicy());
      }
    } else {
      CrossOriginEmbedderPolicyConfig options = objectMapper.convertValue(
        apiSecurityConfig.enableCrossOriginEmbedderPolicy(),
        CrossOriginEmbedderPolicyConfig.class
      );
      middlewares.add(new CrossOriginEmbedderPolicy(options));
    }
  }

  public void processCrossOriginOpenerPolicy(List<Filter> middlewares) {
    if (apiSecurityConfig.enableCrossOriginOpenerPolicy() instanceof Boolean) {
      boolean optionValue =
        (boolean) apiSecurityConfig.enableCrossOriginOpenerPolicy();
      if (optionValue) {
        middlewares.add(new CrossOriginOpenerPolicy());
      }
    } else {
      CrossOriginOpenerPolicyConfig options = objectMapper.convertValue(
        apiSecurityConfig.enableCrossOriginOpenerPolicy(),
        CrossOriginOpenerPolicyConfig.class
      );
      middlewares.add(new CrossOriginOpenerPolicy(options));
    }
  }

  public void processCrossOriginResourcePolicy(List<Filter> middlewares) {
    if (
      apiSecurityConfig.enableCrossOriginResourcePolicy() instanceof Boolean
    ) {
      boolean optionValue =
        (boolean) apiSecurityConfig.enableCrossOriginResourcePolicy();
      if (optionValue) {
        middlewares.add(new CrossOriginResourcePolicy());
      }
    } else {
      CrossOriginResourcePolicyConfig options = objectMapper.convertValue(
        apiSecurityConfig.enableCrossOriginResourcePolicy(),
        CrossOriginResourcePolicyConfig.class
      );
      middlewares.add(new CrossOriginResourcePolicy(options));
    }
  }

  public void processOriginAgentCluster(List<Filter> middlewares) {
    if (
      apiSecurityConfig.enableOriginAgentCluster() == null ||
      apiSecurityConfig.enableOriginAgentCluster()
    ) {
      middlewares.add(new OriginAgentCluster());
    }
  }

  public void processReferrerPolicy(List<Filter> middlewares) {
    if (apiSecurityConfig.enableReferrerPolicy() instanceof Boolean) {
      boolean optionValue = (boolean) apiSecurityConfig.enableReferrerPolicy();
      if (optionValue) {
        middlewares.add(new ReferrerPolicy());
      }
    } else {
      ReferrerPolicyConfig options = objectMapper.convertValue(
        apiSecurityConfig.enableReferrerPolicy(),
        ReferrerPolicyConfig.class
      );
      middlewares.add(new ReferrerPolicy(options));
    }
  }

  public void processStrictTransportSecurity(
    List<Filter> middlewares,
    HttpSecurity http
  ) throws Exception {
    // this is for disabling default spring security behaviour
    http.headers().httpStrictTransportSecurity().disable();
    if (apiSecurityConfig.enableStrictTransportSecurity() instanceof Boolean) {
      boolean optionValue =
        (boolean) apiSecurityConfig.enableStrictTransportSecurity();
      if (optionValue) {
        middlewares.add(new StrictTransportSecurity());
      }
    } else {
      StrictTransportSecurityConfig options = objectMapper.convertValue(
        apiSecurityConfig.enableStrictTransportSecurity(),
        StrictTransportSecurityConfig.class
      );
      middlewares.add(new StrictTransportSecurity(options));
    }
  }

  public void processXContentTypeOptions(
    List<Filter> middlewares,
    HttpSecurity http
  ) throws Exception {
    // this is for disabling default spring security behaviour
    http.headers().contentTypeOptions().disable();
    if (
      apiSecurityConfig.enableXContentTypeOptions() == null ||
      apiSecurityConfig.enableXContentTypeOptions()
    ) {
      middlewares.add(new XContentTypeOptions());
    }
  }

  public void processXDnsPrefetchControl(List<Filter> middlewares) {
    if (apiSecurityConfig.enableXDnsPrefetchControl() instanceof Boolean) {
      boolean optionValue =
        (boolean) apiSecurityConfig.enableXDnsPrefetchControl();
      if (optionValue) {
        middlewares.add(new XDnsPrefetchControl());
      }
    } else {
      XDnsPrefetchControlConfig options = objectMapper.convertValue(
        apiSecurityConfig.enableXDnsPrefetchControl(),
        XDnsPrefetchControlConfig.class
      );
      middlewares.add(new XDnsPrefetchControl(options));
    }
  }

  public void processXDownloadOptions(List<Filter> middlewares) {
    if (
      apiSecurityConfig.enableXDownloadOptions() == null ||
      apiSecurityConfig.enableXDownloadOptions()
    ) {
      middlewares.add(new XDownloadOptions());
    }
  }

  public void processXFrameOptions(List<Filter> middlewares, HttpSecurity http)
    throws Exception {
    // this is for disabling default spring security behaviour
    http.headers().frameOptions().disable();
    if (apiSecurityConfig.enableXFrameOptions() instanceof Boolean) {
      boolean optionValue = (boolean) apiSecurityConfig.enableXFrameOptions();
      if (optionValue) {
        middlewares.add(new XFrameOptions());
      }
    } else {
      XFrameOptionsConfig options = objectMapper.convertValue(
        apiSecurityConfig.enableXFrameOptions(),
        XFrameOptionsConfig.class
      );
      middlewares.add(new XFrameOptions(options));
    }
  }

  public void processXPermittedCrossDomainPolicies(List<Filter> middlewares) {
    if (
      apiSecurityConfig.enableXPermittedCrossDomainPolicies() instanceof Boolean
    ) {
      boolean optionValue =
        (boolean) apiSecurityConfig.enableXPermittedCrossDomainPolicies();
      if (optionValue) {
        middlewares.add(new XPermittedCrossDomainPolicies());
      }
    } else {
      XPermittedCrossDomainPoliciesConfig options = objectMapper.convertValue(
        apiSecurityConfig.enableXPermittedCrossDomainPolicies(),
        XPermittedCrossDomainPoliciesConfig.class
      );
      middlewares.add(new XPermittedCrossDomainPolicies(options));
    }
  }

  public void processXXssProtections(
    List<Filter> middlewares,
    HttpSecurity http
  ) throws Exception {
    // this is for disabling default spring security behaviour
    http.headers().xssProtection().disable();
    if (
      apiSecurityConfig.enableXXssProtection() == null ||
      apiSecurityConfig.enableXXssProtection()
    ) {
      middlewares.add(new XXssProtection());
    }
  }

  public void processXPoweredBy(HttpSecurity http) {
    if (
      apiSecurityConfig.enableXPoweredBy() == null ||
      apiSecurityConfig.enableXPoweredBy()
    ) {
      http.addFilterBefore(new XPoweredBy(), DisableEncodeUrlFilter.class);
    }
  }
}
