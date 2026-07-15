package sn.isepat.etudiants.security;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sn.isepat.etudiants.exception.ErrorResponse;

/** Repond en JSON {"code":401,...} lorsqu'une route protegee est appelee sans token JWT valide. */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		ErrorResponse erreur = new ErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, "Authentification requise. Token JWT manquant ou invalide.");
		response.getWriter().write(objectMapper.writeValueAsString(erreur));
	}
}
