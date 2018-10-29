package fr.unice.polytech.soa.uberoo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "RecipeOrder")
public class Recipe implements Serializable{

	@Column(name = "orderId", nullable = false)
	private Long orderId;
	
	@Column(name = "montant", nullable = false)
	private float montant;
	@Column(name = "timestamp")
	private Date timestamp;
	
	public Recipe(Long orderId, float montant, Date timestamp) {
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
