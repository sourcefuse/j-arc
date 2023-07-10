package com.sourcefuse.jarc.authlib.api.security.middlewares;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.management.InvalidAttributeValueException;

import org.springframework.web.filter.OncePerRequestFilter;

import com.sourcefuse.jarc.authlib.api.security.config.ContentSecurityPolicyConfig;
import com.sourcefuse.jarc.authlib.api.security.types.MiddlewareConstants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContentSecurityPolicy extends OncePerRequestFilter {

  public static final String DANGEROUSLY_DISABLE_DEFAULT_SRC =
    "dangerouslyDisableDefaultSrc";
  private static final String DEFAULT_SRC = "default-src";

  private final String headerName;

  private final Map<String, List<String>> normalizedDirectives;

  public ContentSecurityPolicy() {
    headerName = "Content-Security-Policy";
    normalizedDirectives = normalizeDirectives(null);
  }

  public ContentSecurityPolicy(ContentSecurityPolicyConfig options) {
    headerName =
      options.isReportOnly()
        ? "Content-Security-Policy-Report-Only"
        : "Content-Security-Policy";

    normalizedDirectives = normalizeDirectives(options);
  }

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    String headerValue;
    try {
      headerValue = getHeaderValue(normalizedDirectives);
      response.setHeader(headerName, headerValue);
    } catch (InvalidAttributeValueException e) {
      log.info(e.getMessage(), e);
    }
    filterChain.doFilter(request, response);
  }

  private static boolean isDirectiveValueInvalid(String str) {
    return str.matches(";|,");
  }

  private static String dashify(String str) {
    return str.replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase();
  }

  @SuppressWarnings("unchecked")
  private static Map<String, List<String>> normalizeDirectives(
    ContentSecurityPolicyConfig options
  ) {
    if (options == null) {
      options = new ContentSecurityPolicyConfig();
    }
    Map<String, Object> defaultDirectives = MiddlewareConstants.DEFAULT_CONTENT_SECURITY_POLICY_DIRECTIVE; // getDefaultDirectives();
    if (options.getUseDefaults() == null) {
      options.setUseDefaults(true);
    }
    if (options.getDirectives() == null) {
      options.setDirectives(defaultDirectives);
    }

    Map<String, List<String>> result = new HashMap<>();
    List<String> directiveNamesSeen = new ArrayList<>();
    List<String> directivesExplicitlyDisabled = new ArrayList<>();

    for (Entry<String, Object> entry : options.getDirectives().entrySet()) {
      String rawDirectiveName = entry.getKey();
      String directiveName = dashify(rawDirectiveName);

      if (directiveNamesSeen.contains(directiveName)) {
        throw new IllegalArgumentException(
          "Content-Security-Policy received a duplicate directive " +
          directiveName
        );
      }
      directiveNamesSeen.add(directiveName);

      Object rawDirectiveValue = entry.getValue();
      List<String> directiveValue;
      if (rawDirectiveValue == null) {
        if (directiveName.equals(DEFAULT_SRC)) {
          throw new IllegalArgumentException(
            "Content-Security-Policy needs a default-src but it was set to `null`. " +
            "If you really want to disable it, set it to " +
            "`contentSecurityPolicy.dangerouslyDisableDefaultSrc`."
          );
        }
        directivesExplicitlyDisabled.add(directiveName);
        continue;
      }
      if (rawDirectiveValue instanceof String stringDirectiveValue) {
        if (stringDirectiveValue.isBlank()) {
          throw new IllegalArgumentException(
            "Content-Security-Policy received an invalid directive value for " +
            directiveName
          );
        }
        if (stringDirectiveValue.equals(DANGEROUSLY_DISABLE_DEFAULT_SRC)) {
          if (directiveName.equals(DEFAULT_SRC)) {
            directivesExplicitlyDisabled.add(DEFAULT_SRC);
            continue;
          }
          throw new IllegalArgumentException(
            "Content-Security-Policy: tried to disable " +
            directiveName +
            " as if it were default-src; simply omit the key"
          );
        } else {
          directiveValue = Arrays.asList(stringDirectiveValue);
        }
      } else {
        directiveValue = (List<String>) rawDirectiveValue;
      }
      for (String element : directiveValue) {
        if (isDirectiveValueInvalid(element)) {
          throw new IllegalArgumentException(
            "Content-Security-Policy received an invalid directive value for " +
            directiveName
          );
        }
      }

      result.put(directiveName, directiveValue);
    }

    if (options.getUseDefaults().booleanValue()) {
      defaultDirectives.forEach(
        (String defaultDirectiveName, Object defaultDirectiveValue) -> {
          if (
            !result.containsKey(defaultDirectiveName) &&
            !directivesExplicitlyDisabled.contains(defaultDirectiveName)
          ) {
            result.put(
              defaultDirectiveName,
              (List<String>) defaultDirectiveValue
            );
          }
        }
      );
    }

    if (result.isEmpty()) {
      throw new Error(
        "Content-Security-Policy has no directives. Either set some or disable the header"
      );
    }
    if (
      !result.containsKey(DEFAULT_SRC) &&
      !directivesExplicitlyDisabled.contains(DEFAULT_SRC)
    ) {
      throw new Error(
        "Content-Security-Policy needs a default-src but none was provided. If you really want to disable it, set it to `contentSecurityPolicy.dangerouslyDisableDefaultSrc`."
      );
    }

    return result;
  }

  private static String getHeaderValue(
    Map<String, List<String>> normalizedDirectives
  ) throws InvalidAttributeValueException {
    List<String> result = new ArrayList<>();
    for (Entry<String, List<String>> entry : normalizedDirectives.entrySet()) {
      String directiveName = entry.getKey();
      List<String> rawDirectiveValue = entry.getValue();
      String directiveValue = "";
      for (String element : rawDirectiveValue) {
        directiveValue += " " + element;
      }

      if (directiveValue.isBlank()) {
        result.add(directiveName);
      } else if (isDirectiveValueInvalid(directiveValue)) {
        throw new InvalidAttributeValueException(
          "Content-Security-Policy received an invalid directive value " +
          directiveName
        );
      } else {
        result.add(directiveName + directiveValue);
      }
    }

    return String.join(";", result);
  }
}
