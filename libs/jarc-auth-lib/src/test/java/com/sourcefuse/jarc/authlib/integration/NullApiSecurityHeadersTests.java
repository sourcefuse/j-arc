package com.sourcefuse.jarc.authlib.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.header.writers.CrossOriginEmbedderPolicyHeaderWriter.CrossOriginEmbedderPolicy;
import org.springframework.security.web.header.writers.CrossOriginOpenerPolicyHeaderWriter.CrossOriginOpenerPolicy;
import org.springframework.security.web.header.writers.CrossOriginResourcePolicyHeaderWriter.CrossOriginResourcePolicy;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFrameOptionsMode;
import org.springframework.test.web.servlet.MockMvc;

import com.sourcefuse.jarc.authlib.api.security.header.writers.XPermittedCrossDomainPoliciesHeaderWriter;
import com.sourcefuse.jarc.authlib.test.app.condig.NullApiSecurityAppConfig;

@SpringBootTest
@Import(NullApiSecurityAppConfig.class)
@AutoConfigureMockMvc
public class NullApiSecurityHeadersTests {

	@Autowired
	private MockMvc mockMvc;

	private String MOCK_API_URL = "/fake-url";

	private MockHttpServletResponse getMockResponse() throws Exception {
		return mockMvc.perform(get(MOCK_API_URL)).andReturn().getResponse();
	}

	@Test
	void testResponseHeader_ContentSecurityPolicy() throws Exception {

		MockHttpServletResponse response = getMockResponse();

		assertThat(response.getHeader("Content-Security-Policy")).isNotBlank()
				.isEqualTo("script-src-attr 'none';style-src 'self' https: 'unsafe-inline';"
						+ "base-uri 'self';script-src 'self';object-src 'none';"
						+ "form-action 'self';upgrade-insecure-requests;"
						+ "frame-ancestors 'self';img-src 'self' data:;"
						+ "default-src 'self';font-src 'self' https: data:");
	}

	@Test
	void testResponseHeader_CrossOriginEmbedderPolicy() throws Exception {

		MockHttpServletResponse response = getMockResponse();

		assertThat(response.getHeader("Cross-Origin-Embedder-Policy")).isNotBlank()
				.isEqualTo(CrossOriginEmbedderPolicy.REQUIRE_CORP.getPolicy());
	}

	@Test
	void testResponseHeader_CrossOriginOpenerPolicy() throws Exception {

		MockHttpServletResponse response = getMockResponse();

		assertThat(response.getHeader("Cross-Origin-Opener-Policy")).isNotBlank()
				.isEqualTo(CrossOriginOpenerPolicy.SAME_ORIGIN.getPolicy());
	}

	@Test
	void testResponseHeader_CrossOriginResourcePolicy() throws Exception {

		MockHttpServletResponse response = getMockResponse();

		assertThat(response.getHeader("Cross-Origin-Resource-Policy")).isNotBlank()
				.isEqualTo(CrossOriginResourcePolicy.SAME_ORIGIN.getPolicy());
	}

	@Test
	void testResponseHeader_OriginAgentCluster() throws Exception {

		MockHttpServletResponse response = getMockResponse();

		assertThat(response.getHeader("Origin-Agent-Cluster")).isNotBlank()
				.isEqualTo("?1");
	}

	@Test
	void testResponseHeader_ReferrerPolicy() throws Exception {

		MockHttpServletResponse response = getMockResponse();

		assertThat(response.getHeader("Referrer-Policy")).isNotBlank()
				.isEqualTo(ReferrerPolicy.NO_REFERRER.getPolicy());
	}

	@Test
	void testResponseHeader_StrictTransportSecurity() throws Exception {

		MockHttpServletResponse response = getMockResponse();

		// this header will be blank as spring test app is in http mode
		assertThat(response.getHeader("Strict-Transport-Security")).isNull();
	}

	@Test
	void testResponseHeader_XContentTypeOptions() throws Exception {

		MockHttpServletResponse response = getMockResponse();

		assertThat(response.getHeader("X-Content-Type-Options")).isNotBlank()
				.isEqualTo("nosniff");
	}

	@Test
	void testResponseHeader_XDnsPrefetchControl() throws Exception {

		MockHttpServletResponse response = getMockResponse();

		assertThat(response.getHeader("X-DNS-Prefetch-Control")).isNotBlank()
				.isEqualTo("off");
	}

	@Test
	void testResponseHeader_XDownloadOptions() throws Exception {

		MockHttpServletResponse response = getMockResponse();

		assertThat(response.getHeader("X-Download-Options")).isNotBlank()
				.isEqualTo("noopen");
	}

	@Test
	void testResponseHeader_XFrameOptions() throws Exception {

		MockHttpServletResponse response = getMockResponse();

		assertThat(response.getHeader("X-Frame-Options")).isNotBlank()
				.isEqualTo(XFrameOptionsMode.SAMEORIGIN.toString());
	}

	@Test
	void testResponseHeader_XPermittedCrossDomainPolicies() throws Exception {
		MockHttpServletResponse response = getMockResponse();
		System.out.println(response.getHeaderNames());
		assertThat(response.getHeader("X-Permitted-Cross-Domain-Policies")).isNotBlank()
				.isEqualTo(XPermittedCrossDomainPoliciesHeaderWriter.PermittedPolicy.NONE.getPolicy());
	}

	@Test
	void testResponseHeader_XXssProtection() throws Exception {
		MockHttpServletResponse response = getMockResponse();
		assertThat(response.getHeader("X-XSS-Protection")).isNotBlank()
				.isEqualTo(XXssProtectionHeaderWriter.HeaderValue.DISABLED.toString());
	}

}
