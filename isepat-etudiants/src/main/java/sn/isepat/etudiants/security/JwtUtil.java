package sn.isepat.etudiants.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import sn.isepat.etudiants.entity.Utilisateur;

/** Utilitaire de generation et de validation des tokens JWT. */
@Component
public class JwtUtil {

	private final SecretKey key;
	private final long expirationMs;

	public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expirationMs) {
		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.expirationMs = expirationMs;
	}

	public String genererToken(Utilisateur utilisateur) {
		Date maintenant = new Date();
		Date expiration = new Date(maintenant.getTime() + expirationMs);

		return Jwts.builder()
				.subject(utilisateur.getEmail())
				.claim("role", utilisateur.getRole())
				.claim("nom", utilisateur.getNom())
				.issuedAt(maintenant)
				.expiration(expiration)
				.signWith(key)
				.compact();
	}

	public String extraireEmail(String token) {
		return extraireClaims(token).getSubject();
	}

	public String extraireRole(String token) {
		return extraireClaims(token).get("role", String.class);
	}

	public boolean estValide(String token) {
		try {
			extraireClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	private Claims extraireClaims(String token) {
		return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
}
