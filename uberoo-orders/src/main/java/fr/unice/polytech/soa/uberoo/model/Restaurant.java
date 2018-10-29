package fr.unice.polytech.soa.uberoo.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Restaurant {

	@Column(name = "restaurant_id")
	private Long id;

	@Column(name = "restaurant_name")
	private String name;


	public Restaurant() {

	}

	public Restaurant(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Restaurant{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
