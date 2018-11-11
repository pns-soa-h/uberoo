package fr.unice.polytech.soa.uberoo.model.orders;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class BillingAddress extends Address {
	@NotNull
	private String firstName;
	@NotNull private String lastName;
	private String company;

	public BillingAddress() {}

	public BillingAddress(@NotNull String firstName, @NotNull String lastName, String company, @NotNull String address_1, String address_2, @NotNull String city, @NotNull String postcode, @NotNull String country) {
		super(address_1, address_2, city, postcode, country);
		this.firstName = firstName;
		this.lastName = lastName;
		this.company = company;
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
	public String getCompany() { return company; }
	public void setCompany(String company) { this.company = company; }
}
