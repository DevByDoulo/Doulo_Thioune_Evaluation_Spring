package sn.isepat.etudiants.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sn.isepat.etudiants.entity.Utilisateur;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

	Optional<Utilisateur> findByEmail(String email);

	boolean existsByEmail(String email);
}
