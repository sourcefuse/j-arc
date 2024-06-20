package com.sourcefuse.jarc.authlib.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;

import com.sourcefuse.jarc.authlib.api.security.config.ReferrerPolicyConfigOptions;
import com.sourcefuse.jarc.authlib.api.security.header.utils.ReferrerPolicyHeader;

class ReferrerPolicyHeaderTests {
	@Test
	void testGetHeaderValue_withNullOptions() {
		assertThat(ReferrerPolicyHeader.getHeaderValue(null)).isEqualTo(ReferrerPolicy.NO_REFERRER);
	}
	
	@Test
	void testGetHeaderValue_withOptionsInstanceWithNullValues() {
		ReferrerPolicyConfigOptions options = new ReferrerPolicyConfigOptions();
		assertThat(ReferrerPolicyHeader.getHeaderValue(options)).isEqualTo(ReferrerPolicy.NO_REFERRER);
	}
	
	@Test
	void testGetHeaderValue_withOptions() {
		ReferrerPolicyConfigOptions options = new ReferrerPolicyConfigOptions();
		options.setPolicy(ReferrerPolicy.NO_REFERRER_WHEN_DOWNGRADE);
		assertThat(ReferrerPolicyHeader.getHeaderValue(options)).isEqualTo(ReferrerPolicy.NO_REFERRER_WHEN_DOWNGRADE);
	}
}
