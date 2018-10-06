package fr.unice.polytech.soa.uberoo.model;

import javax.persistence.*;

/**
 * Created by Alexis Couvreur on 9/24/2018.
 */
@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
	@Column(unique = true)
    private String name;

	public Restaurant() {

	}

    public Restaurant(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
