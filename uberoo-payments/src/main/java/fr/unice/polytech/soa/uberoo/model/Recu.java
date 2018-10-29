package fr.unice.polytech.soa.uberoo.model;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class Recu {

	
	private Long orderId;
	private float montant;
	private Date timestamp;
	public Recu(Long orderId, float montant, Date timestamp) {
		super();
		this.orderId = orderId;
		this.montant = montant;
		this.timestamp = timestamp;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public float getMontant() {
		return montant;
	}
	public void setMontant(float montant) {
		this.montant = montant;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
