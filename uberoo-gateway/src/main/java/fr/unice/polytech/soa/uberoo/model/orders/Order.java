package fr.unice.polytech.soa.uberoo.model.orders;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;

/**
 * Order
 * 
 * An order is a composed of :
 * 		- a payment detail
 * 		- a shipping and a billing address
 * 		- a customer id 
 * 		- a status
 *
 * @author Alexis Couvreur
 */
public class Order implements Serializable {

    private static final long serialVersionUID = -5123948121637007869L;

	/**
	 * A unique identifier
	 */
	private Long orderId;

	/**
	 * An order is attached to a client, even if this client entity
	 * contains information like the address, the name, etc.
	 * the actual order's shipping/billing addresses might not be the same
	 * thus we only need the client id reference.
	 * For shipping and billing addresses see
	 * `shippingAddress` and `billingAddress` properties
	 */
	private Long clientId;

	/**
	 * An order is composed of one meal atm
	 */
	@Column()
	private Meal meal;

	/**
	 * What the user pay
	 * actually : subtotal + totalShipping
	 */
	@Column
	private Double total;

	/**
	 * Total minus all kind of taxes
	 */
	private Double subtotal;

	private Double totalShipping;

	private Restaurant restaurant;

	/**
	 * Estimated Time before Arrival
	 * Time in milliseconds for a full scenario :
	 * 1. Order confirmed
	 * 2. Order prepared
	 * 3. Coursier retrieves and delivers
	 */
	private Long eta;

	private Status status;

	/**
	 * `billingAddress` which can be different from 
	 * the `shippingAddress`
	 */
	private Address billingAddress;

	/**
	 * `shippingAddress` which can be different from 
	 * the `billingAddress`
	 */
	private Address shippingAddress;
	
	private PaymentDetails paymentDetails;

	/* TIMESTAMPS */
	private Date createdAt;

	private Date updatedAt;

	private Date completedAt;
	/* /TIMESTAMPS */

	public Order() {}

	public Order(OrderRequest request) {
		this(request.getBillingAddress(), request.getShippingAddress(), request.getClient(), request.getMeal(), request.getRestaurant());
	}

	public Order(Address billingAddress, Address shippingAddress, Long clientId, Meal meal, Restaurant restaurant) {
		this.billingAddress  = billingAddress;
		this.shippingAddress = shippingAddress;
		this.clientId 		 = clientId;
		this.meal 			 = meal;
		this.restaurant		 = restaurant;
		this.paymentDetails  = new PaymentDetails();
		this.status = Status.IN_PROGRESS;
		this.totalShipping = 0.;
		// this.total = meals.stream().mapToDouble(Meal::getPrice).sum();
		this.total = meal.getPrice();
		this.createdAt = new Date(Calendar.getInstance().getTime().getTime());
		this.updatedAt = createdAt;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(Date completedAt) {
		this.completedAt = completedAt;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Meal getMeal() {
		return meal;
	}

	public void setMeal(Meal meal) {
		this.meal = meal;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public Double getTotalShipping() {
		return totalShipping;
	}

	public void setTotalShipping(Double total_shipping) {
		this.totalShipping = total_shipping;
	}

	public PaymentDetails getPaymentDetails() {
		return paymentDetails;
	}

	public void setPaymentDetails(PaymentDetails paymentDetails) {
		this.paymentDetails = paymentDetails;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public Long getEta() {
		return eta;
	}

	public void setEta(Long eta) {
		this.eta = eta;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public enum Status {
		CANCELLED,
		IN_PROGRESS,
		ACCEPTED,
		IN_PREPARATION,
		IN_TRANSIT,
		DELIVERED;

		private static Status[] vals = values();
		public Status next()
		{
			return vals[(this.ordinal()+1) % vals.length];
		}
	}

}
