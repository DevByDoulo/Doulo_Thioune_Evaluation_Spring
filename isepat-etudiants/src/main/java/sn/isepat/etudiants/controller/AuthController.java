package sn.isepat.etudiants.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import sn.isepat.etudiants.dto.LoginRequest;
import sn.isepat.etudiants.dto.RegisterRequest;
import sn.isepat.etudiants.dto.TokenResponse;
import sn.isepat.etudiants.dto.UtilisateurResponse;
import sn.isepat.etudiants.entity.Utilisateur;
import sn.isepat.etudiants.exception.ChampObligatoireException;
import sn.isepat.etudiants.exception.ErrorResponse;
import sn.isepat.etudiants.security.JwtUtil;
import sn.isepat.etudiants.service.UtilisateurService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "Inscription et authentification des utilisateurs")
public class AuthController {

	private final UtilisateurService utilisateurService;
	private final JwtUtil jwtUtil;

	@PostMapping("/register")
	@Operation(summary = "Creer un compte utilisateur", description = "Inscrit un nouvel utilisateur apres verification de l'unicite de l'email.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Utilisateur cree avec succes",
					content = @Content(schema = @Schema(implementation = UtilisateurResponse.class))),
			@ApiResponse(responseCode = "400", description = "Champ obligatoire manquant",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "409", description = "Email deja existant",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	public ResponseEntity<UtilisateurResponse> register(@RequestBody RegisterRequest requete) {
		validerChampsObligatoires(requete);

		Utilisateur utilisateur = Utilisateur.builder()
				.nom(requete.nom())
				.email(requete.email())
				.motDePasse(requete.motDePasse())
				.build();

		Utilisateur cree = utilisateurService.inscrire(utilisateur);
		return ResponseEntity.status(HttpStatus.CREATED).body(UtilisateurResponse.from(cree));
	}

	@PostMapping("/login")
	@Operation(summary = "S'authentifier", description = "Verifie les identifiants et retourne un token JWT.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Authentification reussie",
					content = @Content(schema = @Schema(implementation = TokenResponse.class))),
			@ApiResponse(responseCode = "401", description = "Email ou mot de passe incorrect",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest requete) {
		validerChampsObligatoires(requete);

		Utilisateur utilisateur = utilisateurService.authentifier(requete.email(), requete.motDePasse());
		String token = jwtUtil.genererToken(utilisateur);
		return ResponseEntity.ok(new TokenResponse(token));
	}

	private void validerChampsObligatoires(RegisterRequest requete) {
		if (requete.nom() == null || requete.nom().isBlank()) {
			throw new ChampObligatoireException("Le nom est obligatoire.");
		}
		if (requete.email() == null || requete.email().isBlank()) {
			throw new ChampObligatoireException("L'email est obligatoire.");
		}
		if (requete.motDePasse() == null || requete.motDePasse().isBlank()) {
			throw new ChampObligatoireException("Le mot de passe est obligatoire.");
		}
	}

	private void validerChampsObligatoires(LoginRequest requete) {
		if (requete.email() == null || requete.email().isBlank()) {
			throw new ChampObligatoireException("L'email est obligatoire.");
		}
		if (requete.motDePasse() == null || requete.motDePasse().isBlank()) {
			throw new ChampObligatoireException("Le mot de passe est obligatoire.");
		}
	}
}
