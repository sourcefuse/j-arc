package com.sourcefuse.jarc.authlib.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sourcefuse.jarc.authlib.api.security.config.ContentSecurityPolicyConfigOptions;
import com.sourcefuse.jarc.authlib.api.security.header.utils.ContentSecurityPolicyHeader;

class ContentSecurityPolicyHeaderTests {
	Map<String, Object> directives;

	@BeforeEach
	void setUp() {
		directives = new HashMap<>();
		directives.put("default-src", ContentSecurityPolicyHeader.DANGEROUSLY_DISABLE_DEFAULT_SRC);
		directives.put("object-src", Arrays.asList("'none'"));
		directives.put("script-src", Arrays.asList("'self'", "js.example.com"));
		directives.put("upgrade-insecure-requests", Arrays.asList());
	}

	@Test
	void testGetHeaderValue_withNullOptions() {
		assertThat(ContentSecurityPolicyHeader.getHeaderValue(null))
				.isEqualTo("script-src-attr 'none';style-src 'self' https: 'unsafe-inline';"
						+ "base-uri 'self';script-src 'self';object-src 'none';"
						+ "form-action 'self';upgrade-insecure-requests;"
						+ "frame-ancestors 'self';img-src 'self' data:;"
						+ "default-src 'self';font-src 'self' https: data:");
	}

	@Test
	void testGetHeaderValue_withOptionsInstanceWithNullValues() {
		ContentSecurityPolicyConfigOptions options = new ContentSecurityPolicyConfigOptions();
		assertThat(ContentSecurityPolicyHeader.getHeaderValue(options))
				.isEqualTo("script-src-attr 'none';style-src 'self' https: 'unsafe-inline';"
						+ "base-uri 'self';script-src 'self';object-src 'none';"
						+ "form-action 'self';upgrade-insecure-requests;"
						+ "frame-ancestors 'self';img-src 'self' data:;"
						+ "default-src 'self';font-src 'self' https: data:");
	}

	@Test
	void testGetHeaderValue_withCustomDirectives() {
		ContentSecurityPolicyConfigOptions options = new ContentSecurityPolicyConfigOptions(false, directives, false);
		assertThat(ContentSecurityPolicyHeader.getHeaderValue(options))
				.isEqualTo("script-src 'self' js.example.com;object-src 'none';upgrade-insecure-requests");
	}

	@Test
	void testGetHeaderValue_withCustomDirectivesAndUseDefaults() {
		ContentSecurityPolicyConfigOptions options = new ContentSecurityPolicyConfigOptions(true, directives, false);
		assertThat(ContentSecurityPolicyHeader.getHeaderValue(options))
				.isEqualTo("script-src-attr 'none';style-src 'self' https: 'unsafe-inline';"
						+ "script-src 'self' js.example.com;base-uri 'self';" + "object-src 'none';form-action 'self';"
						+ "upgrade-insecure-requests;frame-ancestors 'self';"
						+ "img-src 'self' data:;font-src 'self' https: data:");
	}

	@Test
	void testGetHeaderValue_withNullValueInDefaultSrcDirectives() {
		directives.put("default-src", null);
		ContentSecurityPolicyConfigOptions options = new ContentSecurityPolicyConfigOptions(false, directives, false);
		assertThrows(IllegalArgumentException.class, () -> ContentSecurityPolicyHeader.getHeaderValue(options));
	}

	@Test
	void testGetHeaderValue_withEmptyValueInScriptSrcDirectives() {
		directives.put("script-src", "");
		ContentSecurityPolicyConfigOptions options = new ContentSecurityPolicyConfigOptions(false, directives, false);
		assertThrows(IllegalArgumentException.class, () -> ContentSecurityPolicyHeader.getHeaderValue(options));
	}

	@Test
	void testGetHeaderValue_withDisabledValueInScriptSrcDirectives() {
		directives.put("script-src", ContentSecurityPolicyHeader.DANGEROUSLY_DISABLE_DEFAULT_SRC);
		ContentSecurityPolicyConfigOptions options = new ContentSecurityPolicyConfigOptions(false, directives, false);
		assertThrows(IllegalArgumentException.class, () -> ContentSecurityPolicyHeader.getHeaderValue(options));
	}

	@Test
	void testGetHeaderValue_withEmptyDirectiveMap() {
		directives = new HashMap<>();
		ContentSecurityPolicyConfigOptions options = new ContentSecurityPolicyConfigOptions(false, directives, false);
		assertThrows(IllegalArgumentException.class, () -> ContentSecurityPolicyHeader.getHeaderValue(options));
	}

	@Test
	void testGetHeaderValue_withDirectiveMapNotContainingDefaultSrc() {
		directives.remove("default-src");
		ContentSecurityPolicyConfigOptions options = new ContentSecurityPolicyConfigOptions(false, directives, false);
		assertThrows(IllegalArgumentException.class, () -> ContentSecurityPolicyHeader.getHeaderValue(options));
	}
}
