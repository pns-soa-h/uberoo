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

	@OneToOne
	private Order order;

	public Coursier() {

	}

	public Long getId() {
		return id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
