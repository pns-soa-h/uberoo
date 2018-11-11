package fr.unice.polytech.soa.uberoo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

/**
 * Meal
 * In an order context a meal has the basic properties : name, description and price
 * but we add the `quantity` because we might want the meal multiple times
 *
 * @author Julien Lemaire
 */
@Entity
public class Meal {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@JsonProperty("mealId")
	private Long id;
	private String name;
	private String description;
	@OneToOne
	private Tag tag;
	@OneToOne
	private Restaurant restaurant;
	private Double price;

	public Meal() {}

	public Meal(Long id, String name, String description, Tag tag, Restaurant restaurant, Double price) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.tag = tag;
		this.restaurant = restaurant;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Meal meal = (Meal) o;
		return Objects.equals(name, meal.name) &&
				Objects.equals(description, meal.description) &&
				Objects.equals(tag, meal.tag) &&
				Objects.equals(restaurant, meal.restaurant) &&
				Objects.equals(price, meal.price);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, description, tag, restaurant, price);
	}

	@Override
	public String toString() {
		return "Meal {" +
				"meal_id=" + id +
				", meal_name='" + name + '\'' +
				", meal_description='" + description + '\'' +
				", meal_tag=" + tag.getLabel() +
				", meal_price=" + price +
				'}';
	}
}
