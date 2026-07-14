package sn.isepat.etudiants.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sn.isepat.etudiants.entity.Etudiant;
import sn.isepat.etudiants.exception.EtudiantNotFoundException;
import sn.isepat.etudiants.repository.EtudiantRepository;
import sn.isepat.etudiants.service.EtudiantService;

@Service
@RequiredArgsConstructor
public class EtudiantServiceImpl implements EtudiantService {

	private final EtudiantRepository etudiantRepository;

	@Override
	public Etudiant ajouter(Etudiant etudiant) {
		return etudiantRepository.save(etudiant);
	}

	@Override
	public Etudiant modifier(Long id, Etudiant etudiant) {
		// rechercher() leve EtudiantNotFoundException (404) si l'id n'existe pas
		Etudiant existant = rechercher(id);
		existant.setMatricule(etudiant.getMatricule());
		existant.setPrenom(etudiant.getPrenom());
		existant.setNom(etudiant.getNom());
		existant.setEmail(etudiant.getEmail());
		existant.setDateNaissance(etudiant.getDateNaissance());
		existant.setLieuNaissance(etudiant.getLieuNaissance());
		existant.setNationalite(etudiant.getNationalite());
		return etudiantRepository.save(existant);
	}

	@Override
	public void supprimer(Long id) {
		Etudiant existant = rechercher(id);
		etudiantRepository.delete(existant);
	}

	@Override
	public Etudiant rechercher(Long id) {
		return etudiantRepository.findById(id)
				.orElseThrow(() -> new EtudiantNotFoundException("Etudiant introuvable avec l'id " + id));
	}

	@Override
	public List<Etudiant> lister() {
		return etudiantRepository.findAll();
	}

	@Override
	public Etudiant rechercherParMatricule(String matricule) {
		return etudiantRepository.findByMatricule(matricule)
				.orElseThrow(() -> new EtudiantNotFoundException("Etudiant introuvable avec le matricule " + matricule));
	}

	@Override
	public List<Etudiant> listerTrieParNom() {
		return etudiantRepository.findAllByOrderByNomAsc();
	}

	@Override
	public boolean matriculeExiste(String matricule) {
		return etudiantRepository.existsByMatricule(matricule);
	}

	@Override
	public boolean emailExiste(String email) {
		return etudiantRepository.existsByEmail(email);
	}

	@Override
	public boolean matriculeExisteChezUnAutre(String matricule, Long id) {
		return etudiantRepository.existsByMatriculeAndIdNot(matricule, id);
	}

	@Override
	public boolean emailExisteChezUnAutre(String email, Long id) {
		return etudiantRepository.existsByEmailAndIdNot(email, id);
	}
}
