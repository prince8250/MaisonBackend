package com.YT.MaisonBackend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
		info = @Info(
				title = "MaisonBackend API",
				description = "REST API documentation for the Maison Hostel Management backend.",
				version = "v1",
				contact = @Contact(name = "MaisonBackend Team")),
		servers = @Server(url = "/"))
public class SwaggerConfig {
}