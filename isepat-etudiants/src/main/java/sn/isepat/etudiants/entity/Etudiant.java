package sn.isepat.etudiants.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entite JPA representant un etudiant de l'ISEP-AT.
 * L'age n'est pas stocke : il est calcule cote client a partir de dateNaissance.
 */
@Entity
@Table(name = "etudiant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Etudiant {

	/** Identifiant technique auto-genere (cle primaire). */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Matricule fonctionnel de l'etudiant : obligatoire et unique. */
	@Column(nullable = false, unique = true)
	private String matricule;

	@Column(nullable = false)
	private String prenom;

	@Column(nullable = false)
	private String nom;

	/** Email de l'etudiant : obligatoire et unique. */
	@Column(nullable = false, unique = true)
	private String email;

	@Column(name = "date_naissance", nullable = false)
	private LocalDate dateNaissance;

	@Column(name = "lieu_naissance", nullable = false)
	private String lieuNaissance;

	@Column(nullable = false)
	private String nationalite;
}
