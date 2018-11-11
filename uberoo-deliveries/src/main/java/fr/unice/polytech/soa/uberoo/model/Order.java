package fr.unice.polytech.soa.uberoo.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Alexis Couvreur on 9/24/2018.
 */
@Entity(name = "OrderDelivery")
public class Order implements Serializable {

    @Id
    private Long id;

	@ElementCollection
	@Column(name = "meals")
	@JoinColumn(name = "meal_id")
	private List<Meal> meals;

    private Coursier coursier;

	@Column(name = "client", nullable = false)
	private Long clientId;

    @Column(name = "eta")
    private Long eta;

	public Order() {
    }

	public List<Meal> getMeals() {
		return meals;
	}

	public void setMeals(List<Meal> meals) {
		this.meals = meals;
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

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

}
