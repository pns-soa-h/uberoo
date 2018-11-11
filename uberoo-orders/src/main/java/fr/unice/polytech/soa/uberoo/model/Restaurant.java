package fr.unice.polytech.soa.uberoo.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

@Embeddable
public class Restaurant {

	@Column(name = "restaurant_id")
	@JsonProperty("restaurantId")
	private Long id;

	@Column(name = "restaurant_name")
	private String name;

	@AttributeOverrides({
			@AttributeOverride(name="address_1", column=@Column(name="restaurant_address_1")),
			@AttributeOverride(name="address_2", column=@Column(name="restaurant_address_2")),
			@AttributeOverride(name="city", column=@Column(name="restaurant_city")),
			@AttributeOverride(name="state", column=@Column(name="restaurant_state")),
			@AttributeOverride(name="postcode", column=@Column(name="restaurant_postcode")),
			@AttributeOverride(name="country", column=@Column(name="restaurant_country"))
	})
	@Embedded @NotNull private Address address;


	public Restaurant() {

	}

	public Restaurant(Long id, String name, Address address) {
		this.id = id;
		this.name = name;
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Restaurant{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
