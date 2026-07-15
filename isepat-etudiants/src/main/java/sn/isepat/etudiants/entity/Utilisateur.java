package sn.isepat.etudiants.entity;

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
 * Entite JPA representant un utilisateur pouvant s'authentifier sur l'API.
 * Le mot de passe est toujours stocke encode (BCrypt), jamais en clair.
 */
@Entity
@Table(name = "utilisateur")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utilisateur {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nom;

	/** Email de l'utilisateur : obligatoire et unique, sert d'identifiant de connexion. */
	@Column(nullable = false, unique = true)
	private String email;

	@Column(name = "mot_de_passe", nullable = false)
	private String motDePasse;

	@Column(nullable = false)
	@Builder.Default
	private String role = "USER";
}
