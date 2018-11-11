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

	@Embedded
	private Location location;

	public Coursier() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
