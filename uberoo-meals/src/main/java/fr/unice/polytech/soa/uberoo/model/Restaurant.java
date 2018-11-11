package fr.unice.polytech.soa.uberoo.model;

import javax.persistence.*;
import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Restaurant that = (Restaurant) o;
		return Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
