package fr.unice.polytech.soa.uberoo.model.orders;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * OrderRequest class is a class used to create an order.
 * It contains only the user required inputs and nothing more.
 *
 * @author Alexis Couvreur
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OrderRequest {

	private Address billingAddress;
	private Address shippingAddress;
	private Long client;
	private Meal meal;
	private Restaurant restaurant;

	public OrderRequest() {

	}

	public OrderRequest(Long client, Meal meal, Restaurant restaurant, Address shippingAddress, Address billingAddress) {
		this.billingAddress = billingAddress;
		this.shippingAddress = shippingAddress;
		this.client = client;
		this.meal = meal;
		this.restaurant = restaurant;
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

	public Long getClient() {
		return client;
	}

	public void setClient(Long clientId) {
		this.client = clientId;
	}

	public Meal getMeal() {
		return meal;
	}

	public void setMeal(Meal meal) {
		this.meal = meal;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	@Override
	public String toString() {
		return "OrderRequest{" +
				"billingAddress=" + billingAddress +
				", \nshippingAddress=" + shippingAddress +
				", \nclient=" + client +
				", \nmeal=" + meal +
				", \nrestaurant=" + restaurant +
				'}';
	}
}
