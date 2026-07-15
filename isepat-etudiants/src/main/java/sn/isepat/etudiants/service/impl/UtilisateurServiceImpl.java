package sn.isepat.etudiants.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sn.isepat.etudiants.entity.Utilisateur;
import sn.isepat.etudiants.exception.AuthentificationException;
import sn.isepat.etudiants.exception.EmailUtilisateurExistantException;
import sn.isepat.etudiants.repository.UtilisateurRepository;
import sn.isepat.etudiants.service.UtilisateurService;

@Service
@RequiredArgsConstructor
public class UtilisateurServiceImpl implements UtilisateurService {

	private final UtilisateurRepository utilisateurRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Utilisateur inscrire(Utilisateur utilisateur) {
		if (utilisateurRepository.existsByEmail(utilisateur.getEmail())) {
			throw new EmailUtilisateurExistantException("Cet email existe deja.");
		}
		utilisateur.setId(null);
		utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
		return utilisateurRepository.save(utilisateur);
	}

	@Override
	public Utilisateur authentifier(String email, String motDePasse) {
		Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
				.orElseThrow(() -> new AuthentificationException("Email ou mot de passe incorrect."));

		if (!passwordEncoder.matches(motDePasse, utilisateur.getMotDePasse())) {
			throw new AuthentificationException("Email ou mot de passe incorrect.");
		}
		return utilisateur;
	}
}
