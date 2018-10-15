package fr.unice.polytech.soa.uberoo.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Representation of coursier, delivering orders to client.
 *
 * @author Julien Lemaire
 */
public class Coursier {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;

	@OneToOne
	private Order order;
}
