package sn.isepat.etudiants.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sn.isepat.etudiants.entity.Etudiant;

/**
 * Acces aux donnees des etudiants. Les methodes existsBy... servent aux
 * controles d'unicite (matricule/email) effectues dans le controleur REST.
 */
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

	boolean existsByMatricule(String matricule);

	boolean existsByEmail(String email);

	/** Unicite du matricule en excluant l'etudiant en cours de modification. */
	boolean existsByMatriculeAndIdNot(String matricule, Long id);

	/** Unicite de l'email en excluant l'etudiant en cours de modification. */
	boolean existsByEmailAndIdNot(String email, Long id);

	Optional<Etudiant> findByMatricule(String matricule);

	/** Bonus : liste des etudiants triee par nom en ordre alphabetique. */
	List<Etudiant> findAllByOrderByNomAsc();
}
