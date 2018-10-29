package fr.unice.polytech.soa.uberoo.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Representation of coursier, delivering orders to client.
 *
 * @author Julien Lemaire
 */
@Entity
public class Coursier implements Serializable {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;

	private String name;

	public Coursier() {

	}

	public Coursier(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Coursier coursier = (Coursier) o;
		return Objects.equals(name, coursier.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
