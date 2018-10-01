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
    /*@OneToOne
    private Tag tag;*/
    private String description;

    public Meal(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public Meal() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    /*
    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
    */

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
