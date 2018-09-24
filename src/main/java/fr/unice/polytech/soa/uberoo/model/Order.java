package fr.unice.polytech.soa.uberoo.model;

import javax.persistence.*;

/**
 * Created by Alexis Couvreur on 9/24/2018.
 */
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @OneToOne
    private Client client;
    @OneToOne
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
