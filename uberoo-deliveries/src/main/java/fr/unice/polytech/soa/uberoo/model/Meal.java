package fr.unice.polytech.soa.uberoo.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Created by Alexis Couvreur on 9/24/2018.
 */
@Entity
public class Meal {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String label;

    @Embedded
	@NotNull
	private Restaurant restaurant;

    @NotNull
    private String description;

    public Meal() {

    }

    public Meal(String label, String description, Restaurant restaurant) {
    	this.label = label;
    	this.description = description;
    	this.restaurant = restaurant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
				Objects.equals(description, meal.description);
	}

	@Override
	public int hashCode() {
		return Objects.hash(label, restaurant, description);
	}
}
