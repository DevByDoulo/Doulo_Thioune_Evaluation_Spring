package sn.isepat.etudiants.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI etudiantsOpenApi() {
		return new OpenAPI().info(new Info()
				.title("API Gestion des Etudiants - ISEP-AT")
				.description("API REST CRUD pour la gestion des etudiants de l'ISEP-AT")
				.version("1.0.0")
				.contact(new Contact().name("ISEP-AT").email("ssidibe@ept.edu.sn")));
	}
}
