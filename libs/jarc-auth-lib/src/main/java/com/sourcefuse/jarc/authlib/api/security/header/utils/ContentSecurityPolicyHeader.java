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

  private static final Map<String, Object> defaultDirectives =
    ApiSecurityConstants.DEFAULT_CONTENT_SECURITY_POLICY_DIRECTIVE;

  private ContentSecurityPolicyHeader() {}

  @SuppressWarnings("unchecked")
  private static Map<String, List<String>> normalizeDirectives(
    ContentSecurityPolicyConfigOptions options
  ) {
    Map<String, List<String>> result = new HashMap<>();
    List<String> directivesExplicitlyDisabled = new ArrayList<>();

    ValidateOrInitOptions(options);

    for (Entry<String, Object> entry : options.getDirectives().entrySet()) {
      String rawDirectiveName = entry.getKey();
      String directiveName = dashify(rawDirectiveName);
      Object rawDirectiveValue = entry.getValue();

      if (rawDirectiveValue == null) {
        directivesExplicitlyDisabled.add(validateNullDirective(directiveName));
        continue;
      }
      List<String> directiveValue;
      if (rawDirectiveValue instanceof String stringDirectiveValue) {
        List<String> validatedDirectiveValues = validateStringDirectiveValue(
          directiveName,
          stringDirectiveValue,
          directivesExplicitlyDisabled
        );
        if (validatedDirectiveValues == null) {
          continue;
        } else {
          directiveValue = validatedDirectiveValues;
        }
      } else if (rawDirectiveValue instanceof List<?> stringDirectiveValue) {
        directiveValue = (List<String>) rawDirectiveValue;
      } else {
        throw new IllegalArgumentException(
          "Content-Security-Policy has directive: " +
          directiveName +
          " contains invalid values either provide string or list or string"
        );
      }

      result.put(directiveName, validateDirectiveValues(directiveValue));
    }

    setDefaultValues(
      result,
      options.getUseDefaults().booleanValue(),
      directivesExplicitlyDisabled
    );
    validateNormalizedDirectiveResult(result, directivesExplicitlyDisabled);

    return result;
  }

  private static List<String> validateDirectiveValues(
    List<String> directiveValues
  ) {
    List<String> invalidValues = directiveValues
      .stream()
      .filter(directiveValue -> isDirectiveValueInvalid(directiveValue))
      .toList();

    if (invalidValues.size() > 0) {
      throw new IllegalArgumentException(
        "Content-Security-Policy received an invalid directive value for " +
        String.join(", ", invalidValues)
      );
    }
    return directiveValues;
  }

  @SuppressWarnings("unchecked")
  private static void setDefaultValues(
    Map<String, List<String>> result,
    boolean useDefaults,
    List<String> directivesExplicitlyDisabled
  ) {
    if (useDefaults) {
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
  }

  private static void validateNormalizedDirectiveResult(
    Map<String, List<String>> result,
    List<String> directivesExplicitlyDisabled
  ) {
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

  private static boolean isDirectiveValueInvalid(String str) {
    return Pattern.matches(INVALID_DIRECTIVE_PATTERN, str);
  }

  private static String dashify(String str) {
    return DASHIFY_PATTERN.matcher(str).replaceAll(DASHIFY_REPLACE_WITH);
  }

  private static void ValidateOrInitOptions(
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
  }

  private static String validateNullDirective(String directiveName) {
    if (directiveName.equals(DEFAULT_SRC)) {
      throw new IllegalArgumentException(
        "Content-Security-Policy needs a default-src but it was set to `null`. " +
        "If you really want to disable it, set it to " +
        "`contentSecurityPolicy.dangerouslyDisableDefaultSrc`."
      );
    }
    return directiveName;
  }

  private static List<String> validateStringDirectiveValue(
    String directiveName,
    String stringDirectiveValue,
    List<String> directivesExplicitlyDisabled
  ) {
    if (stringDirectiveValue.isBlank()) {
      throw new IllegalArgumentException(
        "Content-Security-Policy received an invalid directive value for " +
        directiveName
      );
    }
    if (stringDirectiveValue.equals(DANGEROUSLY_DISABLE_DEFAULT_SRC)) {
      if (directiveName.equals(DEFAULT_SRC)) {
        directivesExplicitlyDisabled.add(DEFAULT_SRC);
        // returning null to skip the loop where this method has been called
        return null;
      }
      throw new IllegalArgumentException(
        "Content-Security-Policy: tried to disable " +
        directiveName +
        " as if it were default-src; simply omit the key"
      );
    }
    return Arrays.asList(stringDirectiveValue);
  }
}
