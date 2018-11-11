package fr.unice.polytech.soa.uberoo.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Address {

	@NotNull
	protected String address_1;
	protected String address_2;
	@NotNull protected String city;
	@NotNull protected String postcode;
	@NotNull protected String country;

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
