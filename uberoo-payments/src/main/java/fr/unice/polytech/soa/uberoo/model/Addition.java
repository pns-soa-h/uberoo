package fr.unice.polytech.soa.uberoo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


public class Addition implements Serializable {

	private Long orderId;
    
    private Float montant;
    
	private Method paymentMethod;
	
	private String paymentInfo;
    
    
    
    
    public Addition(Long orderId, Float montant, Method paymentMethod, String paymentInfo) {
		super();
		this.orderId = orderId;
		this.montant = montant;
		this.paymentMethod = paymentMethod;
		this.paymentInfo = paymentInfo;
	}




	public enum Method{
    	CREDIT_CARD
    }




	public Long getOrderId() {
		return orderId;
	}




	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}




	public Float getMontant() {
		return montant;
	}




	public void setMontant(Float montant) {
		this.montant = montant;
	}




	public Method getPaymentMethod() {
		return paymentMethod;
	}




	public void setPaymentMethod(Method paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	



	public String getPaymentInfo() {
		return paymentInfo;
	}




	public void setPaymentInfo(String paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

}
