package sn.isepat.etudiants.exception;

public class EmailExistantException extends RuntimeException {

	public EmailExistantException(String message) {
		super(message);
	}
}
