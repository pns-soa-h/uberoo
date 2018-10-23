package fr.unice.polytech.soa.uberoo.model;

import javax.persistence.*;

/**
 * Created by Alexis Couvreur on 9/24/2018.
 */
@Embeddable
public class Restaurant {

	@Column(unique = true)
    private String name;

	public Restaurant() {

	}

    public Restaurant(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
