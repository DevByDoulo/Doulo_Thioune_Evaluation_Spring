package sn.isepat.etudiants.security;

import java.io.IOException;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/** Extrait et valide le token JWT present dans l'entete Authorization, puis peuple le contexte de securite. */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String PREFIXE_BEARER = "Bearer ";

	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		String entete = request.getHeader("Authorization");

		if (entete != null && entete.startsWith(PREFIXE_BEARER)) {
			String token = entete.substring(PREFIXE_BEARER.length());

			if (jwtUtil.estValide(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
				String email = jwtUtil.extraireEmail(token);
				String role = jwtUtil.extraireRole(token);

				var authentication = new UsernamePasswordAuthenticationToken(
						email, null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		filterChain.doFilter(request, response);
	}
}
