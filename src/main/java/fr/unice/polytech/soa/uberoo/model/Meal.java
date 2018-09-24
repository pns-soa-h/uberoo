package fr.unice.polytech.soa.uberoo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by Alexis Couvreur on 9/24/2018.
 */
@Entity
public class Meal {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String label;
    private String description;

    public Meal(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

}
