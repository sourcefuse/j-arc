package com.sourcefuse.jarc.authlib.api.security.config;

import com.sourcefuse.jarc.authlib.api.security.header.writers.XPermittedCrossDomainPoliciesHeaderWriter.PermittedPolicy;
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
public class XPermittedCrossDomainPoliciesConfig {

  private PermittedPolicy permittedPolicies;
}
