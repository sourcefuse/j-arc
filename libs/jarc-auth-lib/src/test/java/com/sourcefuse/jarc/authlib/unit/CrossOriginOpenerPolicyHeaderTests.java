package com.sourcefuse.jarc.authlib.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.web.header.writers.CrossOriginOpenerPolicyHeaderWriter.CrossOriginOpenerPolicy;

import com.sourcefuse.jarc.authlib.api.security.config.CrossOriginOpenerPolicyConfigOptions;
import com.sourcefuse.jarc.authlib.api.security.header.utils.CrossOriginOpenerPolicyHeader;

public class CrossOriginOpenerPolicyHeaderTests {
	@Test
	void testGetHeaderValue_withNullOptions() {
		assertThat(CrossOriginOpenerPolicyHeader.getHeaderValue(null)).isEqualTo(CrossOriginOpenerPolicy.SAME_ORIGIN);
	}
	
	@Test
	void testGetHeaderValue_withOptionsInstanceWithNullValues() {
		CrossOriginOpenerPolicyConfigOptions options = new CrossOriginOpenerPolicyConfigOptions();
		assertThat(CrossOriginOpenerPolicyHeader.getHeaderValue(options)).isEqualTo(CrossOriginOpenerPolicy.SAME_ORIGIN);
	}
	
	@Test
	void testGetHeaderValue_withOptions() {
		CrossOriginOpenerPolicyConfigOptions options = new CrossOriginOpenerPolicyConfigOptions();
		options.setPolicy(CrossOriginOpenerPolicy.SAME_ORIGIN_ALLOW_POPUPS);
		assertThat(CrossOriginOpenerPolicyHeader.getHeaderValue(options)).isEqualTo(CrossOriginOpenerPolicy.SAME_ORIGIN_ALLOW_POPUPS);
	}
}
