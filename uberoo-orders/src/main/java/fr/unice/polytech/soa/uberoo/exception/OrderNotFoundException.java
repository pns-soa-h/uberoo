package fr.unice.polytech.soa.uberoo.exception;

/**
 * Created by Alexis Couvreur on 10/5/2018.
 */
public class OrderNotFoundException extends RuntimeException {
	private long id;

	public OrderNotFoundException(long id) {
		this.id = id;
	}

	@Override
	public String getMessage() {
		return "Order with id " + this.id + " was not found.";
	}
}
