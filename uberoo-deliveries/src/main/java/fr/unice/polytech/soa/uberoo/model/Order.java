package fr.unice.polytech.soa.uberoo.model;

import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Alexis Couvreur on 9/24/2018.
 */
@Entity(name = "OrderDelivery")
public class Order implements Serializable {

    @Id
    private Long id;

    @Embedded
    private Meal meal;

    private Coursier coursier;

    @Column(name = "eta")
    private Long eta;

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

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
        return id;
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
