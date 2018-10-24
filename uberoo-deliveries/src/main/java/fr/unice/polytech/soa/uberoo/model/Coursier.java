package fr.unice.polytech.soa.uberoo.model;

import javax.persistence.*;

/**
 * Representation of coursier, delivering orders to client.
 *
 * @author Julien Lemaire
 */
@Entity
public class Coursier {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;

	private String name;

	public Coursier() {

	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
