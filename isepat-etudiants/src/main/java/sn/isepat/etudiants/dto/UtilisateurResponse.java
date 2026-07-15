package sn.isepat.etudiants.dto;

import sn.isepat.etudiants.entity.Utilisateur;

/** Vue publique d'un utilisateur, sans le mot de passe. */
public record UtilisateurResponse(Long id, String nom, String email, String role) {

	public static UtilisateurResponse from(Utilisateur utilisateur) {
		return new UtilisateurResponse(utilisateur.getId(), utilisateur.getNom(), utilisateur.getEmail(), utilisateur.getRole());
	}
}
