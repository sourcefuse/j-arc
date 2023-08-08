package com.sourcefuse.jarc.authlib.api.security.header.writers;

import com.sourcefuse.jarc.authlib.api.security.config.XPermittedCrossDomainPoliciesConfigOptions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.util.Assert;

public final class XPermittedCrossDomainPoliciesHeaderWriter
  implements HeaderWriter {

  private static final String X_PERMITTED_DROSS_DOMAIN_POLICIES_HEADER =
    "X-Permitted-Cross-Domain-Policies";

  private PermittedPolicy policy;

  public XPermittedCrossDomainPoliciesHeaderWriter() {
    this.setPolicy(PermittedPolicy.NONE);
  }

  public XPermittedCrossDomainPoliciesHeaderWriter(
    XPermittedCrossDomainPoliciesConfigOptions options
  ) {
    this.setPolicy(options.getPermittedPolicies());
  }

  /**
   * Sets the policy to be used in the response header.
   * @param policy a referrer policy
   * @throws IllegalArgumentException if policy is null
   */
  public void setPolicy(PermittedPolicy policy) {
    Assert.notNull(policy, "policy can not be null");
    this.policy = policy;
  }

  /**
   * @see org.springframework.security.web.header.HeaderWriter#writeHeaders(HttpServletRequest,
   * HttpServletResponse)
   */
  @Override
  public void writeHeaders(
    HttpServletRequest request,
    HttpServletResponse response
  ) {
    if (!response.containsHeader(X_PERMITTED_DROSS_DOMAIN_POLICIES_HEADER)) {
      response.setHeader(
        X_PERMITTED_DROSS_DOMAIN_POLICIES_HEADER,
        this.policy.getPolicy()
      );
    }
  }

  public enum PermittedPolicy {
    NONE("none"),

    MASTER_ONLY("master-only"),

    BY_CONTENT_TYPE("by-content-type"),

    ALL("all");

    private static final Map<String, PermittedPolicy> PERMITTED_POLICIES;

    static {
      Map<String, PermittedPolicy> permittedPolicies = new HashMap<>();
      for (PermittedPolicy referrerPolicy : values()) {
        permittedPolicies.put(referrerPolicy.getPolicy(), referrerPolicy);
      }
      PERMITTED_POLICIES = Collections.unmodifiableMap(permittedPolicies);
    }

    private final String policy;

    PermittedPolicy(String policy) {
      this.policy = policy;
    }

    public String getPolicy() {
      return this.policy;
    }

    public static PermittedPolicy get(String referrerPolicy) {
      return PERMITTED_POLICIES.get(referrerPolicy);
    }
  }

  @Override
  public String toString() {
    return (
      getClass().getName() + " [headerValue=" + this.policy.getPolicy() + "]"
    );
  }
}
