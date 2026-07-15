package sn.isepat.etudiants.service;

import sn.isepat.etudiants.entity.Utilisateur;

/** Logique metier d'inscription et d'authentification des utilisateurs. */
public interface UtilisateurService {

	Utilisateur inscrire(Utilisateur utilisateur);

	Utilisateur authentifier(String email, String motDePasse);
}
