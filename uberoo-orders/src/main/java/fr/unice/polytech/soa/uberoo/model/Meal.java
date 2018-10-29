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
	@Column(name = "meal_name")
	private String name;
	@Column(name = "meal_description")
	private String description;
	@Column(name = "meal_price")
	private Double price;
	@Column(name = "meal_quantity")
	private Integer quantity;

	public Meal() {}

	public Meal(Long id, String name, String description, Double price, Integer quantity) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
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
				", meal_name='" + name + '\'' +
				", meal_description='" + description + '\'' +
				", meal_price=" + price +
				", meal_quantity=" + quantity +
				'}';
	}
}
