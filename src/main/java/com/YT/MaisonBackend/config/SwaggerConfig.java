package com.YT.MaisonBackend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

import org.springframework.context.annotation.Bean;

@Configuration
@OpenAPIDefinition(
		info = @Info(
				title = "MaisonBackend API",
				description = "REST API documentation for the Maison Hostel Management backend.",
				version = "v1",
				contact = @Contact(name = "MaisonBackend Team")),
		servers = @Server(url = "/"))
public class SwaggerConfig {

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
	
	@Bean
	public HttpClient httpClient() {
		return HttpClient.newHttpClient();
	}
}