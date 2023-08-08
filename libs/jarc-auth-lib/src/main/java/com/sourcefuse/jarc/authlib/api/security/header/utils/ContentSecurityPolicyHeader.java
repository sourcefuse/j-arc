package com.sourcefuse.jarc.authlib.api.security.header.utils;

import com.sourcefuse.jarc.authlib.api.security.config.ContentSecurityPolicyConfigOptions;
import com.sourcefuse.jarc.authlib.api.security.types.ApiSecurityConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import javax.management.InvalidAttributeValueException;

public final class ContentSecurityPolicyHeader {

  public static final String DANGEROUSLY_DISABLE_DEFAULT_SRC =
    "dangerouslyDisableDefaultSrc";
  private static final String DEFAULT_SRC = "default-src";
  private static final String INVALID_DIRECTIVE_PATTERN = "[;,]";
  private static final Pattern DASHIFY_PATTERN = Pattern.compile(
    "([a-z])([A-Z])"
  );
  private static final String DASHIFY_REPLACE_WITH = "$1-$2";

  private ContentSecurityPolicyHeader() {}

  private static boolean isDirectiveValueInvalid(String str) {
    return Pattern.matches(INVALID_DIRECTIVE_PATTERN, str);
  }

  private static String dashify(String str) {
    return DASHIFY_PATTERN.matcher(str).replaceAll(DASHIFY_REPLACE_WITH);
  }

  @SuppressWarnings("unchecked")
  private static Map<String, List<String>> normalizeDirectives(
    ContentSecurityPolicyConfigOptions options
  ) {
    if (options == null) {
      options = new ContentSecurityPolicyConfigOptions();
    }
    Map<String, Object> defaultDirectives =
      ApiSecurityConstants.DEFAULT_CONTENT_SECURITY_POLICY_DIRECTIVE;
    if (options.getUseDefaults() == null) {
      options.setUseDefaults(true);
    }
    if (options.getDirectives() == null) {
      options.setDirectives(defaultDirectives);
    }

    Map<String, List<String>> result = new HashMap<>();
    List<String> directivesExplicitlyDisabled = new ArrayList<>();

    for (Entry<String, Object> entry : options.getDirectives().entrySet()) {
      String rawDirectiveName = entry.getKey();
      String directiveName = dashify(rawDirectiveName);
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
      throw new IllegalArgumentException(
        "Content-Security-Policy has no directives. Either set some or disable the header"
      );
    }
    if (
      !result.containsKey(DEFAULT_SRC) &&
      !directivesExplicitlyDisabled.contains(DEFAULT_SRC)
    ) {
      throw new IllegalArgumentException(
        "Content-Security-Policy needs a default-src but none was provided." +
        "If you really want to disable it, set it to `contentSecurityPolicy." +
        "dangerouslyDisableDefaultSrc`."
      );
    }

    return result;
  }

  public static String getHeaderValue(
    ContentSecurityPolicyConfigOptions options
  ) throws InvalidAttributeValueException {
    Map<String, List<String>> normalizedDirectives = normalizeDirectives(
      options
    );
    List<String> result = new ArrayList<>();
    for (Entry<String, List<String>> entry : normalizedDirectives.entrySet()) {
      String directiveName = entry.getKey();
      List<String> rawDirectiveValue = entry.getValue();
      String directiveValue = String.join(" ", rawDirectiveValue);

      if (directiveValue.isBlank()) {
        result.add(directiveName);
      } else if (isDirectiveValueInvalid(directiveValue)) {
        throw new InvalidAttributeValueException(
          "Content-Security-Policy received an invalid directive value " +
          directiveName
        );
      } else {
        result.add(directiveName + " " + directiveValue);
      }
    }

    return String.join(";", result);
  }
}
