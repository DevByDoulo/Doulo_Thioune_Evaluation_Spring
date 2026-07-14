package sn.isepat.etudiants.service;

import java.util.List;

import sn.isepat.etudiants.entity.Etudiant;

/** Logique metier de gestion des etudiants (CRUD + controles d'unicite). */
public interface EtudiantService {

	Etudiant ajouter(Etudiant etudiant);

	Etudiant modifier(Long id, Etudiant etudiant);

	void supprimer(Long id);

	Etudiant rechercher(Long id);

	List<Etudiant> lister();

	Etudiant rechercherParMatricule(String matricule);

	List<Etudiant> listerTrieParNom();

	boolean matriculeExiste(String matricule);

	boolean emailExiste(String email);

	boolean matriculeExisteChezUnAutre(String matricule, Long id);

	boolean emailExisteChezUnAutre(String email, Long id);
}
