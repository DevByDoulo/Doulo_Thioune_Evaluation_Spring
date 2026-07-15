package sn.isepat.etudiants.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

	private static final String BEARER_AUTH = "bearerAuth";

	@Bean
	public OpenAPI etudiantsOpenApi() {
		return new OpenAPI()
				.info(new Info()
						.title("API Gestion des Etudiants - ISEP-AT")
						.description("API REST CRUD pour la gestion des etudiants de l'ISEP-AT, securisee par JWT")
						.version("1.0.0")
						.contact(new Contact().name("ISEP-AT").email("ssidibe@ept.edu.sn")))
				.addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH))
				.components(new Components().addSecuritySchemes(BEARER_AUTH,
						new SecurityScheme()
								.name(BEARER_AUTH)
								.type(SecurityScheme.Type.HTTP)
								.scheme("bearer")
								.bearerFormat("JWT")));
	}
}
