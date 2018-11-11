package fr.unice.polytech.soa.uberoo.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Meal
 * In an order context a meal has the basic properties : name, description and price
 * but we add the `quantity` because we might want the meal multiple times
 *
 * @author Alexis Couvreur
 */
@Embeddable
public class Meal {

	@Column(name = "meal_id")
	private Long id;

	public Meal() {}

	public Meal(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Meal {" +
				"meal_id=" + id +
				'}';
	}
}
