package fr.unice.polytech.soa.uberoo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

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
@Entity(name = "OrderMeal")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Order implements Serializable {

    private static final long serialVersionUID = -5123948121637007869L;

	/**
	 * A unique identifier
	 */
	@Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id", nullable = false)
	private Long id;

	/**
	 * An order is attached to a client, even if this client entity
	 * contains information like the address, the name, etc.
	 * the actual order's shipping/billing addresses might not be the same
	 * thus we only need the client id reference.
	 * For shipping and billing addresses see
	 * `shippingAddress` and `billingAddress` properties
	 */
	@Column(name = "client", nullable = false)
	private Long clientId;

	/**
	 * An order is composed of one meal atm
	 */
	@ElementCollection
	@Column(name = "meals")
	@JoinColumn(name = "meal_id")
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
	@Column
	private Double subtotal;

	@Column(name="total_shipping")
	private Double totalShipping;

	@Embedded
	private Coupon coupon;

	/**
	 * To be removed
	 */
	@Deprecated
	@Column(name = "coursier")
	private Long coursierId;


	@Embedded
	private Restaurant restaurant;

	/**
	 * Estimated Time before Arrival
	 * Time in milliseconds for a full scenario :
	 * 1. Order confirmed
	 * 2. Order prepared
	 * 3. Coursier retrieves and delivers
	 */
	@Column(name = "eta")
	private Long eta;

	@Enumerated(EnumType.STRING)
	private Status status;

	/**
	 * `billingAddress` which can be different from 
	 * the `shippingAddress`
	 */
	@AttributeOverrides({
			@AttributeOverride(name="firstName", column=@Column(name="billing_firstName")),
			@AttributeOverride(name="lastName", column=@Column(name="billing_lastName")),
			@AttributeOverride(name="company", column=@Column(name="billing_company")),
			@AttributeOverride(name="address_1", column=@Column(name="billing_address_1")),
			@AttributeOverride(name="address_2", column=@Column(name="billing_address_2")),
			@AttributeOverride(name="city", column=@Column(name="billing_city")),
			@AttributeOverride(name="state", column=@Column(name="billing_state")),
			@AttributeOverride(name="postcode", column=@Column(name="billing_postcode")),
			@AttributeOverride(name="country", column=@Column(name="billing_country"))
	})
	@Embedded private BillingAddress billingAddress;

	/**
	 * `shippingAddress` which can be different from 
	 * the `billingAddress`
	 */
	@AttributeOverrides({
			@AttributeOverride(name="firstName", column=@Column(name="shipping_firstName")),
			@AttributeOverride(name="lastName", column=@Column(name="shipping_lastName")),
			@AttributeOverride(name="company", column=@Column(name="shipping_company")),
			@AttributeOverride(name="address_1", column=@Column(name="shipping_address_1")),
			@AttributeOverride(name="address_2", column=@Column(name="shipping_address_2")),
			@AttributeOverride(name="city", column=@Column(name="shipping_city")),
			@AttributeOverride(name="state", column=@Column(name="shipping_state")),
			@AttributeOverride(name="postcode", column=@Column(name="shipping_postcode")),
			@AttributeOverride(name="country", column=@Column(name="shipping_country")),
			@AttributeOverride(name="email", column=@Column(name="shipping_email")),
			@AttributeOverride(name="phone", column=@Column(name="shipping_phone"))
	})
	@Embedded private ShippingAddress shippingAddress;
	
	@Embedded
	private PaymentDetails paymentDetails;

	/* TIMESTAMPS */
	@Column(name = "created_at")
	@CreationTimestamp
	private Date createdAt;

	@Column(name = "updated_at")
	@UpdateTimestamp
	private Date updatedAt;

	@Column(name = "completed_at", columnDefinition = "DATE default NULL")
	@UpdateTimestamp
	private Date completedAt;
	/* /TIMESTAMPS */

	public Order() {
		this.paymentDetails  = new PaymentDetails();
		this.createdAt = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		this.updatedAt = createdAt;
		this.status = Status.IN_PROGRESS;
	}

	/*public Order(OrderRequest request) {
		this(request.getBillingAddress(), request.getShippingAddress(), request.getClient(), request.getMeal(), request.getRestaurant());
	}*/

	public Order(BillingAddress billingAddress, ShippingAddress shippingAddress, Long clientId, List<Meal> meals, Restaurant restaurant) {
		this.billingAddress  = billingAddress;
		this.shippingAddress = shippingAddress;
		this.clientId 		 = clientId;
		this.meals 			 = meals;
		this.restaurant		 = restaurant;
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

	public BillingAddress getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(BillingAddress billingAddress) {
		this.billingAddress = billingAddress;
	}

	public ShippingAddress getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(ShippingAddress shippingAddress) {
		this.shippingAddress = shippingAddress;
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

	public PaymentDetails getPaymentDetails() {
		return paymentDetails;
	}

	public void setPaymentDetails(PaymentDetails paymentDetails) {
		this.paymentDetails = paymentDetails;
	}

	@Deprecated
	public Long getCoursierId() {
		return coursierId;
	}

	@Deprecated
	public void setCoursierId(Long coursierId) {
		this.coursierId = coursierId;
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
		public Status next()
		{
			return vals[(this.ordinal()+1) % vals.length];
		}
	}

}
