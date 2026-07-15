package sn.isepat.etudiants.controller;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import sn.isepat.etudiants.dto.LoginRequest;
import sn.isepat.etudiants.dto.RegisterRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifie le parcours de securisation JWT (Tests 7 a 12 du sujet) :
 * inscription, email deja existant, authentification, mauvais mot de passe,
 * appel protege sans token, appel protege avec token.
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

	private static final String MOT_DE_PASSE = "Passw0rd!";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void inscriptionRetourne201() throws Exception {
		mockMvc.perform(post("/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(corpsInscription(emailUnique())))
				.andExpect(status().isCreated());
	}

	@Test
	void inscriptionAvecEmailExistantRetourne409() throws Exception {
		String email = emailUnique();
		inscrire(email);

		mockMvc.perform(post("/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(corpsInscription(email)))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.code").value(409));
	}

	@Test
	void authentificationValideRetourneUnToken() throws Exception {
		String email = emailUnique();
		inscrire(email);

		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(corpsConnexion(email, MOT_DE_PASSE)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").isNotEmpty());
	}

	@Test
	void authentificationAvecMauvaisMotDePasseRetourne401() throws Exception {
		String email = emailUnique();
		inscrire(email);

		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(corpsConnexion(email, "mauvais-mot-de-passe")))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.code").value(401));
	}

	@Test
	void appelSansTokenRetourne401() throws Exception {
		mockMvc.perform(get("/etudiants"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void appelAvecTokenRetourne200() throws Exception {
		String email = emailUnique();
		inscrire(email);
		String token = seConnecterEtRecupererToken(email);

		mockMvc.perform(get("/etudiants")
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk());
	}

	private void inscrire(String email) throws Exception {
		mockMvc.perform(post("/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(corpsInscription(email)))
				.andExpect(status().isCreated());
	}

	private String seConnecterEtRecupererToken(String email) throws Exception {
		String reponse = mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(corpsConnexion(email, MOT_DE_PASSE)))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		return objectMapper.readTree(reponse).get("token").asText();
	}

	private String corpsInscription(String email) throws Exception {
		return objectMapper.writeValueAsString(new RegisterRequest("Utilisateur Test", email, MOT_DE_PASSE));
	}

	private String corpsConnexion(String email, String motDePasse) throws Exception {
		return objectMapper.writeValueAsString(new LoginRequest(email, motDePasse));
	}

	private String emailUnique() {
		return "test-" + UUID.randomUUID() + "@isepat.sn";
	}
}
