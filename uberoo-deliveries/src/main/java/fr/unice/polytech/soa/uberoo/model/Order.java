package fr.unice.polytech.soa.uberoo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Alexis Couvreur on 9/24/2018.
 */
@Entity(name = "OrderDelivery")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "meal", nullable = false)
    @OneToOne
    private Meal meal;

    @Column(name = "coursier")
	@OneToOne
    private Coursier coursier;

    @Column(name = "restaurant", nullable = false)
    @OneToOne
	private Restaurant restaurant;

    @Column(name = "eta")
    private Long eta;

	@Enumerated(EnumType.STRING)
	private Status status;

	public Order() {
    }

	public Meal getMeal() {
		return meal;
	}

	public void setMeal(Meal meal) {
		this.meal = meal;
	}

	public Coursier getCoursier() {
		return coursier;
	}

	public void setCoursier(Coursier coursier) {
		this.coursier = coursier;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public Long getId() {
        return id;
    }

    public Status getStatus() {
    	return this.status;
	}

	public void setStatus(Status status) {
    	this.status = status;
	}

	public Long getETA() {
    	return eta;
	}

	public void setETA(Long eta) {
    	this.eta = eta;
	}

	public enum Status {
		IN_PROGRESS,
		COMPLETED,
		CANCELLED,
		ASSIGNED
	}

}
