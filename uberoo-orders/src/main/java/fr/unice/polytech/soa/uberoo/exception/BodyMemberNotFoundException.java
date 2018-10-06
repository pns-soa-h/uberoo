package fr.unice.polytech.soa.uberoo.exception;

/**
 * Created by Alexis Couvreur on 10/5/2018.
 */

public class BodyMemberNotFoundException extends RuntimeException {
	private String header;

	public BodyMemberNotFoundException(String header) {
		this.header = header;
	}

	@Override
	public String getMessage() {
		return "Required header '" + this.header + "' was not found.";
	}
}
