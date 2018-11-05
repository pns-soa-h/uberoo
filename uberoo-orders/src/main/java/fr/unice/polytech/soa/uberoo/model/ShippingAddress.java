package fr.unice.polytech.soa.uberoo.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class ShippingAddress extends BillingAddress {

	@NotNull private String email;
	@NotNull private String phone;

	public ShippingAddress() {}

	public ShippingAddress(@NotNull String firstName, @NotNull String lastName, String company, @NotNull String address_1, String address_2, @NotNull String city, @NotNull String postcode, @NotNull String country, @NotNull String email, @NotNull String phone) {
		super(firstName, lastName, company, address_1, address_2, city, postcode, country);
		this.email = email;
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
