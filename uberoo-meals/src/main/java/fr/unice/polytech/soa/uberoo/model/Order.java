package fr.unice.polytech.soa.uberoo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

/**
 * Order
 * <p>
 * An order is a composed of :
 * - a payment detail
 * - a shipping and a billing address
 * - a customer id
 * - a status
 *
 * @author Alexis Couvreur
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Order implements Serializable {

	private static final long serialVersionUID = -5123948121637007869L;

	/**
	 * A unique identifier
	 */
	private Long id;

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
	private List<Meal> meals;

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

	private Coupon coupon;

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

	private Date createdAt;

	private Date updatedAt;

	private Date completedAt;
	/* /TIMESTAMPS */

	public Order() {
		this.createdAt = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		this.updatedAt = createdAt;
		this.status = Status.IN_PROGRESS;
	}

	/*public Order(OrderRequest request) {
		this(request.getBillingAddress(), request.getShippingAddress(), request.getClient(), request.getMeal(), request.getRestaurant());
	}*/

	public Order(Long clientId, List<Meal> meals, Restaurant restaurant) {
		this.clientId = clientId;
		this.meals = meals;
		this.restaurant = restaurant;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public List<Meal> getMeals() {
		return meals;
	}

	public void setMeals(List<Meal> meals) {
		this.meals = meals;
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

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public enum Status {
		CANCELLED,
		IN_PROGRESS,
		ACCEPTED,
		IN_PREPARATION,
		IN_TRANSIT,
		DELIVERED;

		private static Status[] vals = values();

		public Status next() {
			return vals[(this.ordinal() + 1) % vals.length];
		}
	}

}
