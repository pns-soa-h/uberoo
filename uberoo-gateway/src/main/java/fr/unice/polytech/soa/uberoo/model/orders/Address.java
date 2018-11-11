package fr.unice.polytech.soa.uberoo.model.orders;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * Address class
 * 
 * Describes a complete address with all needed information
 *
 * @author Alexis Couvreur
 */
public class Address {

	private String firstName;

	private String lastName;

	private String address_1;

	private String address_2;

	private String city;

	private String state;

	private String postcode;

	private String country;

	private String email;

	private String phone;

	public Address() {}

	public Address(@NotNull String firstName, @NotNull String lastName, @NotNull String address_1, String address_2, @NotNull String city, @NotNull String state, @NotNull String postcode, @NotNull String country, @NotNull String email, @NotNull String phone) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address_1 = address_1;
		this.address_2 = address_2;
		this.city = city;
		this.state = state;
		this.postcode = postcode;
		this.country = country;
		this.email = email;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	@Override
	public String toString() {
		return "Address{" +
				"firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", address_1='" + address_1 + '\'' +
				", address_2='" + address_2 + '\'' +
				", city='" + city + '\'' +
				", state='" + state + '\'' +
				", postcode='" + postcode + '\'' +
				", country='" + country + '\'' +
				", email='" + email + '\'' +
				", phone='" + phone + '\'' +
				'}';
	}
}
