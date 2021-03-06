package fr.unice.polytech.soa.uberoo.model;

import fr.unice.polytech.soa.uberoo.model.Addition.Method;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "RecipeOrder")
public class Recipe implements Serializable{

	@Id
	@Column(name = "orderId", nullable = false)
	private Long orderId;
	
	@Column(name = "montant", nullable = false)
	private float montant;
	@Column(name = "timestamp")
	private Date timestamp;
	@Enumerated(EnumType.STRING)
	private Method paymentMethod;
	
	public Recipe(Long orderId, float montant, Date timestamp, Method paymentMethod) {
		super();
		this.orderId = orderId;
		this.montant = montant;
		this.timestamp = timestamp;
		this.paymentMethod = paymentMethod;
	}
	public Method getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(Method paymentMethod) {
		this.paymentMethod = paymentMethod;
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
