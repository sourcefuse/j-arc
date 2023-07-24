package com.basic.example.facadeserviceexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class FacadeServiceExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(FacadeServiceExampleApplication.class, args);
	}

	@Bean
	public WebClient initWebClient() {
		// Create a WebClient instance
		return WebClient.builder().build();
	}

}
