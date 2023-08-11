package com.sourcefuse.jarc.authlib.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.web.header.writers.CrossOriginResourcePolicyHeaderWriter.CrossOriginResourcePolicy;

import com.sourcefuse.jarc.authlib.api.security.config.CrossOriginResourcePolicyConfigOptions;
import com.sourcefuse.jarc.authlib.api.security.header.utils.CrossOriginResourcePolicyHeader;

public class CrossOriginResourcePolicyHeaderTests {
	@Test
	void testGetHeaderValue_withNullOptions() {
		assertThat(CrossOriginResourcePolicyHeader.getHeaderValue(null)).isEqualTo(CrossOriginResourcePolicy.SAME_ORIGIN);
	}
	
	@Test
	void testGetHeaderValue_withOptionsInstanceWithNullValues() {
		CrossOriginResourcePolicyConfigOptions options = new CrossOriginResourcePolicyConfigOptions();
		assertThat(CrossOriginResourcePolicyHeader.getHeaderValue(options)).isEqualTo(CrossOriginResourcePolicy.SAME_ORIGIN);
	}
	
	@Test
	void testGetHeaderValue_withOptions() {
		CrossOriginResourcePolicyConfigOptions options = new CrossOriginResourcePolicyConfigOptions();
		options.setPolicy(CrossOriginResourcePolicy.SAME_SITE);
		assertThat(CrossOriginResourcePolicyHeader.getHeaderValue(options)).isEqualTo(CrossOriginResourcePolicy.SAME_SITE);
	}
}

