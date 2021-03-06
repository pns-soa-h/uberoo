package fr.unice.polytech.soa.uberoo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Alexis Couvreur on 9/24/2018.
 */
@Entity(name = "OrderMeal")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "client", nullable = false)
    private Long clientId;

    @Column(name = "meal", nullable = false)
    private Long mealId;

    @Column(name = "coursier")
    private Long coursierId;

    @Column(name = "restaurant", nullable = false)
	private Long restaurantId;

    @Column(name = "eta")
    private Long eta;

	@Enumerated(EnumType.STRING)
	private Status status;

	public Order() {
    }

    public Order(Long clientId, Long mealId, Long restaurantId) {
        this.clientId = clientId;
        this.mealId = mealId;
        this.restaurantId = restaurantId; // Must specify the restaurant in charge
    }

    public Long getId() {
        return id;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setMealIt(Long mealId) {
        this.mealId = mealId;
    }

    public Long getMealId() {
        return mealId;
    }

    public Long getCoursierId() { return coursierId; }

    public void setCoursierId(Long coursierId) { this.coursierId = coursierId; }

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

	public Long getRestaurantId() {
    	return restaurantId;
	}

	public void setRestaurantId(Long restaurantId) {
		this.restaurantId = restaurantId;
	}
	public enum Status {
		IN_PROGRESS,
		COMPLETED,
		CANCELLED,
		ASSIGNED
	}

}
