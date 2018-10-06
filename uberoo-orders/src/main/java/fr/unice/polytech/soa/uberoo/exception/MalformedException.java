package fr.unice.polytech.soa.uberoo.exception;

/**
 * Created by Alexis Couvreur on 10/5/2018.
 */
public class MalformedException extends RuntimeException {
	private final String expected;
	private final String header;

	public MalformedException(String header, String expected) {
		this.header = header;
		this.expected = expected;
	}

	@Override
	public String getMessage() {
		return "Header '" + header + "' is malformed. Expected '" + expected + "'";
	}
}
