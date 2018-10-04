package fr.unice.polytech.soa.uberoo.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Alexis Couvreur on 9/24/2018.
 */
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "meal_id", nullable = false)
    private Long mealId;

    @Column(name = "coursier_id")
    private Long coursierId;

    public Order(Long clientId, Long mealId) {
        this.clientId = clientId;
        this.mealId = mealId;
    }

    public Long getId() {
        return id;
    }

    public Long getClientId() {
        return clientId;
    }

    public Long getMealId() {
        return mealId;
    }

    public Long getCoursierId() { return coursierId; }

    public void setCoursierId(Long coursierId) { this.coursierId = coursierId; }

}
