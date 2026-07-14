package sn.isepat.etudiants.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import sn.isepat.etudiants.entity.Etudiant;
import sn.isepat.etudiants.exception.ChampObligatoireException;
import sn.isepat.etudiants.exception.EmailExistantException;
import sn.isepat.etudiants.exception.ErrorResponse;
import sn.isepat.etudiants.exception.MatriculeExistantException;
import sn.isepat.etudiants.service.EtudiantService;

@RestController
@RequestMapping("/etudiants")
@RequiredArgsConstructor
@Tag(name = "Etudiants", description = "Gestion des etudiants de l'ISEP-AT")
public class EtudiantController {

	private final EtudiantService etudiantService;

	@PostMapping
	@Operation(summary = "Ajouter un etudiant", description = "Cree un nouvel etudiant apres verification des champs obligatoires et de l'unicite du matricule et de l'email.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Etudiant cree avec succes",
					content = @Content(schema = @Schema(implementation = Etudiant.class))),
			@ApiResponse(responseCode = "400", description = "Champ obligatoire manquant",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "409", description = "Matricule ou email deja existant",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	public ResponseEntity<Etudiant> ajouter(@RequestBody Etudiant etudiant) {
		// Controles manuels demandes par le sujet (pas encore de @Valid a ce stade du cours)
		validerChampsObligatoires(etudiant);

		if (etudiantService.matriculeExiste(etudiant.getMatricule())) {
			throw new MatriculeExistantException("Le matricule existe deja.");
		}
		if (etudiantService.emailExiste(etudiant.getEmail())) {
			throw new EmailExistantException("L'email existe deja.");
		}

		etudiant.setId(null);
		Etudiant cree = etudiantService.ajouter(etudiant);
		return ResponseEntity.status(HttpStatus.CREATED).body(cree);
	}

	@GetMapping
	@Operation(summary = "Lister les etudiants", description = "Retourne la liste de tous les etudiants. Utiliser le parametre 'tri=nom' pour trier par ordre alphabetique du nom.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Liste des etudiants",
					content = @Content(schema = @Schema(implementation = Etudiant.class)))
	})
	public ResponseEntity<List<Etudiant>> lister(
			@Parameter(description = "Valeur 'nom' pour trier la liste par nom en ordre alphabetique")
			@org.springframework.web.bind.annotation.RequestParam(required = false) String tri) {
		List<Etudiant> etudiants = "nom".equalsIgnoreCase(tri) ? etudiantService.listerTrieParNom() : etudiantService.lister();
		return ResponseEntity.ok(etudiants);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Rechercher un etudiant par id", description = "Retourne l'etudiant correspondant a l'identifiant technique fourni.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Etudiant trouve",
					content = @Content(schema = @Schema(implementation = Etudiant.class))),
			@ApiResponse(responseCode = "404", description = "Etudiant introuvable",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	public ResponseEntity<Etudiant> rechercher(@PathVariable Long id) {
		return ResponseEntity.ok(etudiantService.rechercher(id));
	}

	@GetMapping("/matricule/{matricule}")
	@Operation(summary = "Rechercher un etudiant par matricule", description = "Retourne l'etudiant correspondant au matricule fourni.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Etudiant trouve",
					content = @Content(schema = @Schema(implementation = Etudiant.class))),
			@ApiResponse(responseCode = "404", description = "Etudiant introuvable",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	public ResponseEntity<Etudiant> rechercherParMatricule(@PathVariable String matricule) {
		return ResponseEntity.ok(etudiantService.rechercherParMatricule(matricule));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Modifier un etudiant", description = "Met a jour un etudiant existant apres verification des champs obligatoires et de l'unicite du matricule et de l'email.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Etudiant modifie avec succes",
					content = @Content(schema = @Schema(implementation = Etudiant.class))),
			@ApiResponse(responseCode = "400", description = "Champ obligatoire manquant",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "404", description = "Etudiant introuvable",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "409", description = "Matricule ou email deja utilise par un autre etudiant",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	public ResponseEntity<Etudiant> modifier(@PathVariable Long id, @RequestBody Etudiant etudiant) {
		validerChampsObligatoires(etudiant);

		etudiantService.rechercher(id);

		if (etudiantService.matriculeExisteChezUnAutre(etudiant.getMatricule(), id)) {
			throw new MatriculeExistantException("Le matricule existe deja.");
		}
		if (etudiantService.emailExisteChezUnAutre(etudiant.getEmail(), id)) {
			throw new EmailExistantException("L'email existe deja.");
		}

		return ResponseEntity.ok(etudiantService.modifier(id, etudiant));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Supprimer un etudiant", description = "Supprime l'etudiant correspondant a l'identifiant fourni.")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Etudiant supprime avec succes"),
			@ApiResponse(responseCode = "404", description = "Etudiant introuvable",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	public ResponseEntity<Void> supprimer(@PathVariable Long id) {
		etudiantService.supprimer(id);
		return ResponseEntity.noContent().build();
	}

	/** Verifie manuellement chaque champ obligatoire et remonte le premier defaut trouve (400). */
	private void validerChampsObligatoires(Etudiant etudiant) {
		if (etudiant.getMatricule() == null || etudiant.getMatricule().isBlank()) {
			throw new ChampObligatoireException("Le matricule est obligatoire.");
		}
		if (etudiant.getPrenom() == null || etudiant.getPrenom().isBlank()) {
			throw new ChampObligatoireException("Le prenom est obligatoire.");
		}
		if (etudiant.getNom() == null || etudiant.getNom().isBlank()) {
			throw new ChampObligatoireException("Le nom est obligatoire.");
		}
		if (etudiant.getEmail() == null || etudiant.getEmail().isBlank()) {
			throw new ChampObligatoireException("L'email est obligatoire.");
		}
		if (etudiant.getDateNaissance() == null) {
			throw new ChampObligatoireException("La date de naissance est obligatoire.");
		}
		if (etudiant.getLieuNaissance() == null || etudiant.getLieuNaissance().isBlank()) {
			throw new ChampObligatoireException("Le lieu de naissance est obligatoire.");
		}
		if (etudiant.getNationalite() == null || etudiant.getNationalite().isBlank()) {
			throw new ChampObligatoireException("La nationalite est obligatoire.");
		}
	}
}
