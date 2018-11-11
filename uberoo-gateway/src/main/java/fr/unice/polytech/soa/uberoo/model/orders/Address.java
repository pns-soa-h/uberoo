package fr.unice.polytech.soa.uberoo.model.orders;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Address {

	@NotNull
	private String address_1;
	private String address_2;
	@NotNull private String city;
	@NotNull private String postcode;
	@NotNull private String country;

	public Address() {}

	public Address(@NotNull String address_1, String address_2, @NotNull String city, @NotNull String postcode, @NotNull String country) {
		this.address_1 = address_1;
		this.address_2 = address_2;
		this.city = city;
		this.postcode = postcode;
		this.country = country;
	}

	public String getAddress_1() {
		return address_1;
	}
	public void setAddress_1(String address_1) {
		this.address_1 = address_1;
	}
	public String getAddress_2() {
		return address_2;
	}
	public void setAddress_2(String address_2) {
		this.address_2 = address_2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
}
