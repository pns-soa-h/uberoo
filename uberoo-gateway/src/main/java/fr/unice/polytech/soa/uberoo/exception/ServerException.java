package fr.unice.polytech.soa.uberoo.exception;

import org.springframework.http.ResponseEntity;

/**
 * Generic server exception
 *
 * @author Julien Lemaire
 */
public class ServerException extends RuntimeException {

	private ResponseEntity responseEntity;

	public ServerException(ResponseEntity responseEntity) {
		this.responseEntity = responseEntity;
	}

	@Override
	public String getMessage() {
		return responseEntity.getHeaders()+ "\n" + responseEntity.getBody();
	}
}
