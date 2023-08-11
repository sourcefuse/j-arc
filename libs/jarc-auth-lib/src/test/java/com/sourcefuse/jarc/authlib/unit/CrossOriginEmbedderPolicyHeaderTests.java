package com.sourcefuse.jarc.authlib.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.web.header.writers.CrossOriginEmbedderPolicyHeaderWriter.CrossOriginEmbedderPolicy;

import com.sourcefuse.jarc.authlib.api.security.config.CrossOriginEmbedderPolicyConfigOptions;
import com.sourcefuse.jarc.authlib.api.security.header.utils.CrossOriginEmbedderPolicyHeader;

public class CrossOriginEmbedderPolicyHeaderTests {
	@Test
	void testGetHeaderValue_withNullOptions() {
		assertThat(CrossOriginEmbedderPolicyHeader.getHeaderValue(null))
				.isEqualTo(CrossOriginEmbedderPolicy.REQUIRE_CORP);
	}

	@Test
	void testGetHeaderValue_withOptionsInstanceWithNullValues() {
		CrossOriginEmbedderPolicyConfigOptions options = new CrossOriginEmbedderPolicyConfigOptions();
		assertThat(CrossOriginEmbedderPolicyHeader.getHeaderValue(options))
				.isEqualTo(CrossOriginEmbedderPolicy.REQUIRE_CORP);
	}

	@Test
	void testGetHeaderValue_withOptions() {
		CrossOriginEmbedderPolicyConfigOptions options = new CrossOriginEmbedderPolicyConfigOptions();
		options.setPolicy(CrossOriginEmbedderPolicy.UNSAFE_NONE);
		assertThat(CrossOriginEmbedderPolicyHeader.getHeaderValue(options))
				.isEqualTo(CrossOriginEmbedderPolicy.UNSAFE_NONE);
	}
}
