package fr.unice.polytech.soa.uberoo.exception;

/**
 * Thrown if meal is not found.
 *
 * @author Julien Lemaire
 */
public class MealNotFoundException extends RuntimeException {
	private long id;

	public MealNotFoundException(long id) {
		this.id = id;
	}

	@Override
	public String getMessage() {
		return "Meal with id " + this.id + " cannot be found.";
	}
}
