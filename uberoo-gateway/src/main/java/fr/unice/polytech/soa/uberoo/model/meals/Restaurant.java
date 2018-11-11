package fr.unice.polytech.soa.uberoo.model.meals;

import javax.persistence.*;

/**
 * Created by Alexis Couvreur on 9/24/2018.
 */
public class Restaurant {

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
