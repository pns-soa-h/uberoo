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
	@Column(name = "meal_quantity")
	private Integer quantity;

	public Meal() {}

	public Meal(Long id, Integer quantity) {
		this.id = id;
		this.quantity = quantity;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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
				", meal_quantity=" + quantity +
				'}';
	}
}
