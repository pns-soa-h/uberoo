package fr.unice.polytech.soa.uberoo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity(name = "PaymentOrder")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Payment implements Serializable {
	
    @Column(name = "order", nullable = false)
    private Long orderId;
    
    @Column(name = "montant", nullable = false)
    private Float montant;
    
    @Enumerated(EnumType.STRING)
	private Method paymentMethod;
    
    
    public enum Method{
    	CREDIT_CARD
    }

}
