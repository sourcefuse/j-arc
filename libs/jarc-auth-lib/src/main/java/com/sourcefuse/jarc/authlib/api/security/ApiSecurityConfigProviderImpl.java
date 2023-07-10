package com.sourcefuse.jarc.authlib.api.security;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

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
import com.sourcefuse.jarc.authlib.api.security.types.MiddlewareConstants;

@Service
public class ApiSecurityConfigProviderImpl implements ApiSecurityConfig {

	@SuppressWarnings("unchecked")
	@Override
	public Object enableContentSecurityPolicy() {
		Map<String, Object> directives = Stream
				.of(new Object[][] { { "default-src", Arrays.asList("'self'", "default.example") },
						{ "object-src", Arrays.asList("'none'") },
						{ "script-src", Arrays.asList("'self'", "js.example.com") },
						{ "upgrade-insecure-requests", Arrays.asList() } })
				.collect(Collectors.toMap(data -> (String) data[0], data -> (List<String>) data[1]));
//		return new ContentSecurityPolicyConfig(false, directives, false);
		return false;
	}

	@Override
	public Object enableCrossOriginEmbedderPolicy() {
		return false;
//		return new CrossOriginEmbedderPolicyConfig("credentialless");
	}

	@Override
	public Object enableCrossOriginOpenerPolicy() {

		return false;
//		return new CrossOriginOpenerPolicyConfig("same-origin-allow-popups");
	}

	@Override
	public Object enableCrossOriginResourcePolicy() {

		return false;
//		return new CrossOriginResourcePolicyConfig("cross-origin");
	}

	@Override
	public Boolean enableOriginAgentCluster() {

		return false;
//		return true;
	}

	@Override
	public Object enableReferrerPolicy() {

		return false;
//		return new ReferrerPolicyConfig(MiddlewareConstants.ALLOWED_REFFERER_POLICY_TOKENS);
	}

	@Override
	public Object enableStrictTransportSecurity() {

		return false;
//		return new StrictTransportSecurityConfig(1800D, true, true);
	}

	@Override
	public Boolean enableXContentTypeOptions() {

		return false;
//		return true;
	}

	@Override
	public Object enableXDnsPrefetchControl() {

		return false;
//		return new XDnsPrefetchControlConfig(true);
	}

	@Override
	public Object enableXFrameOptions() {

		return false;
//		return new XFrameOptionsConfig("sameorigin");
	}

	@Override
	public Boolean enableXDownloadOptions() {

		return false;
//		return true;
	}

	@Override
	public Object enableXPermittedCrossDomainPolicies() {

		return false;
//		return new XPermittedCrossDomainPoliciesConfig("all");
	}

	@Override
	public Boolean enableXPoweredBy() {

		return false;
//		return true;
	}

	@Override
	public Boolean enableXXssProtection() {

		return false;
//		return true;
	}
}
