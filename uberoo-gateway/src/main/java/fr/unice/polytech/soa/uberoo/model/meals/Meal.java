package fr.unice.polytech.soa.uberoo.model.meals;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

/**
 * Meal
 * In an order context a meal has the basic properties : name, description and price
 * but we add the `quantity` because we might want the meal multiple times
 *
 * @author Julien Lemaire
 */
public class Meal {

	private Long mealId;
	private String name;
	private String description;
	private Tag tag;
	private Restaurant restaurant;
	private Double price;

	public Meal() {}

	public Meal(Long mealId, String name, String description, Tag tag, Double price) {
		this.mealId = mealId;
		this.name = name;
		this.description = description;
		this.price = price;
		this.tag = tag;
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

	public Long getMealId() {
		return mealId;
	}

	public void setMealId(Long mealId) {
		this.mealId = mealId;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	@Override
	public String toString() {
		return "Meal {" +
				"meal_id=" + mealId +
				", meal_name='" + name + '\'' +
				", meal_description='" + description + '\'' +
				", meal_tag=" + tag.getLabel() +
				", meal_price=" + price +
				'}';
	}
}
