package com.sourcefuse.jarc.authlib.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.sourcefuse.jarc.authlib.test.app.condig.DisabledApiSecurityAppConfig;

@SpringBootTest
@Import(DisabledApiSecurityAppConfig.class)
@AutoConfigureMockMvc
public class DisabledApiSecurityHeadersTests {

	@Autowired
	private MockMvc mockMvc;

	private String MOCK_API_URL = "/fake-url";

	private MockHttpServletResponse getMockResponse() throws Exception {
		return mockMvc.perform(get(MOCK_API_URL)).andReturn().getResponse();
	}

	@Test
	void testResponseHeader_APISecurityHeaderShouldBeNull() throws Exception {

		MockHttpServletResponse response = getMockResponse();

		checkIfHeaderIsNull(response,"Content-Security-Policy");

		checkIfHeaderIsNull(response,"Cross-Origin-Embedder-Policy");

		checkIfHeaderIsNull(response,"Cross-Origin-Opener-Policy");

		checkIfHeaderIsNull(response,"Cross-Origin-Resource-Policy");

		checkIfHeaderIsNull(response,"Origin-Agent-Cluster");

		checkIfHeaderIsNull(response,"Referrer-Policy");

		checkIfHeaderIsNull(response,"X-Content-Type-Options");

		checkIfHeaderIsNull(response,"X-DNS-Prefetch-Control");

		checkIfHeaderIsNull(response,"X-Download-Options");

		checkIfHeaderIsNull(response,"X-Frame-Options");

		checkIfHeaderIsNull(response,"X-Permitted-Cross-Domain-Policies");

		checkIfHeaderIsNull(response,"X-XSS-Protection");
	}

	void checkIfHeaderIsNull(MockHttpServletResponse response, String headerName){
		assertThat(response.getHeader(headerName)).isNull();
	}
}
