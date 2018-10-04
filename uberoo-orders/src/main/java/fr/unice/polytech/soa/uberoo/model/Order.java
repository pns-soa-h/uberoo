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

    public Order() {
    }

    public Order(Long clientId, Long mealId) {
        this.clientId = clientId;
        this.mealId = mealId;
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

}
