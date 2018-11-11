package fr.unice.polytech.soa.uberoo.model;

<<<<<<< HEAD
import javax.persistence.*;
=======
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
>>>>>>> 424bc4b... Coursier account
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

<<<<<<< HEAD
	@Column(name = "client", nullable = false)
	private Long clientId;
=======
    private Status status;
>>>>>>> 424bc4b... Coursier account

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

<<<<<<< HEAD
	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
=======

	public void setStatus(Status status) {
		this.status = status;
	}

	public enum Status {
		IN_PROGRESS,
		COMPLETED,
		CANCELLED,
		ASSIGNED
>>>>>>> 424bc4b... Coursier account
	}

}
