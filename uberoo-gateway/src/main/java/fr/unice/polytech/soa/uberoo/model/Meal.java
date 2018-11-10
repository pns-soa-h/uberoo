package fr.unice.polytech.soa.uberoo.model;

import java.util.Objects;

/**
 * Created by Alexis Couvreur on 9/24/2018.
 */
public class Meal {

    private long mealId;

    private String label;

	private Restaurant restaurant;

    private Tag tag;

    private String description;

    public Meal() {

    }

    public Meal(long mealId, String label, String description, Restaurant restaurant) {
    	this.mealId = mealId;
    	this.label = label;
    	this.description = description;
    	this.restaurant = restaurant;
    }

    public Meal(long mealId, String label, String description, Restaurant restaurant, Tag tag) {
    	this(mealId, label, description, restaurant);
    	this.tag = tag;
    }

    public long getMealId() {
        return mealId;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
		return Objects.equals(label, meal.label) &&
				Objects.equals(restaurant, meal.restaurant) &&
				Objects.equals(tag, meal.tag) &&
				Objects.equals(description, meal.description);
	}

	@Override
	public int hashCode() {
		return Objects.hash(label, restaurant, tag, description);
	}
}
