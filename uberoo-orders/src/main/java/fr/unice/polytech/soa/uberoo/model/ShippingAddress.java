package fr.unice.polytech.soa.uberoo.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class ShippingAddress extends Address {

	@NotNull private String email;
	@NotNull private String phone;
	@NotNull
	private String firstName;
	@NotNull
	private String lastName;

	public ShippingAddress() {}

	public ShippingAddress(@NotNull String firstName, @NotNull String lastName, String email, String phone, @NotNull String address_1, String address_2, @NotNull String city, @NotNull String postcode, @NotNull String country) {
		super(address_1, address_2, city, postcode, country);
		this.firstName = firstName;
		this.lastName = lastName;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
