package fr.unice.polytech.soa.uberoo.exception;

public class CoursierNotFoundException extends RuntimeException {
	private long id;

	public CoursierNotFoundException(long id) {
		this.id = id;
	}

	@Override
	public String getMessage() {
		return "Order with id " + this.id + " was not found.";
	}
}
