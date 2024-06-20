package com.sourcefuse.jarc.authlib.api.security.config;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ContentSecurityPolicyConfigOptions {

  private Boolean useDefaults;

  // key value pair, value can be string or List<string>
  private Map<String, Object> directives;

  private boolean reportOnly;
}
