package sn.isepat.etudiants.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Convertit les exceptions metier levees par le controleur en reponses HTTP
 * standardisees {"code": ..., "msg": ...}, conformement au sujet.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ChampObligatoireException.class)
	public ResponseEntity<ErrorResponse> handleChampObligatoire(ChampObligatoireException ex) {
		return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler(MatriculeExistantException.class)
	public ResponseEntity<ErrorResponse> handleMatriculeExistant(MatriculeExistantException ex) {
		return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
	}

	@ExceptionHandler(EmailExistantException.class)
	public ResponseEntity<ErrorResponse> handleEmailExistant(EmailExistantException ex) {
		return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
	}

	@ExceptionHandler(EtudiantNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleEtudiantNotFound(EtudiantNotFoundException ex) {
		return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
		return ResponseEntity.status(status).body(new ErrorResponse(status.value(), message));
	}
}
