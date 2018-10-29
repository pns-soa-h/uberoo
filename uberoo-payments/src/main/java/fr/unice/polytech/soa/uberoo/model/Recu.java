package fr.unice.polytech.soa.uberoo.model;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class Recu {

	
	private Long orderId;
	private float montant;
	private Date timestamp;

}
