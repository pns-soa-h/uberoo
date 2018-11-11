package fr.unice.polytech.soa.uberoo.exception;

public class CouponNotFoundException extends RuntimeException {
	private long id;

	public CouponNotFoundException(long id) {
		this.id = id;
	}

	@Override
	public String getMessage() {
		return "Coupon with id " + this.id + " cannot be found.";
	}
}
