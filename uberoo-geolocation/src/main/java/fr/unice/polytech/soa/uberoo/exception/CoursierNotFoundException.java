package fr.unice.polytech.soa.uberoo.exception;

/**
 * Created by Alexis Couvreur on 10/5/2018.
 */
public class CoursierNotFoundException extends RuntimeException {

	private long id;

	public CoursierNotFoundException(long id) {
		this.id = id;
	}

	@Override
	public String getMessage() {
		return "Coursier with id " + this.id + " was not found.";
	}
}
