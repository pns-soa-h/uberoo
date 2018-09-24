package fr.unice.polytech.soa.uberoo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by Alexis Couvreur on 9/24/2018.
 */
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private Client client;
    private Meal meal;

    public Order(Client client, Meal meal) {
        this.client = client;
        this.meal = meal;
    }

    public Long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public Meal getMeal() {
        return meal;
    }

}
