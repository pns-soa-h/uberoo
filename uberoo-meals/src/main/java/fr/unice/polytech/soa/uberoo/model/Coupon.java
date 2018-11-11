package fr.unice.polytech.soa.uberoo.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author Alexis Couvreur
 */
@Entity
public class Coupon {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * The code to be used by
	 * customers
	 */
	@Column(unique = true, nullable = false)
	private String code;

	/**
	 * The restaurant that owns the coupon
	 * which only applies to the restaurant's meals
	 */
	@NotNull
	private Long restaurantId;

	/**
	 * The amount of discount
	 * can be fixed or percentage
	 */
	@NotNull
	private Double amount;

	/**
	 * The type of the discount
	 * see enum description
	 */
	@Enumerated(EnumType.STRING)
	private DiscountType discountType;

	/**
	 * The description of the coupon
	 */
	private String description;

	@Column(name = "created_at")
	@CreationTimestamp
	private Date date_created;

	@Column(name = "updated_at")
	@UpdateTimestamp
	private Date date_modified;

	@Temporal(TemporalType.DATE)
	private Date date_expires;

	@Column(columnDefinition = "boolean default false", nullable = false)
	private Boolean freeShipping = false;

	public Coupon() {
	}

	public Coupon(String code, @NotNull Long restaurantId, @NotNull Double amount, DiscountType discountType, String description, Date date_expires, Boolean freeShipping) {
		this.code = code;
		this.restaurantId = restaurantId;
		this.amount = amount;
		this.discountType = discountType;
		this.description = description;
		this.date_expires = date_expires;
		this.freeShipping = freeShipping;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Long restaurantId) {
		this.restaurantId = restaurantId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public DiscountType getDiscountType() {
		return discountType;
	}

	public void setDiscountType(DiscountType discountType) {
		this.discountType = discountType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate_created() {
		return date_created;
	}

	public void setDate_created(Date date_created) {
		this.date_created = date_created;
	}

	public Date getDate_modified() {
		return date_modified;
	}

	public void setDate_modified(Date date_modified) {
		this.date_modified = date_modified;
	}

	public Date getDate_expires() {
		return date_expires;
	}

	public void setDate_expires(Date date_expires) {
		this.date_expires = date_expires;
	}

	public Boolean getFreeShipping() {
		return freeShipping;
	}

	public void setFreeShipping(Boolean freeShipping) {
		this.freeShipping = freeShipping;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Returns how much you can take off
	 * @param meals
	 * @param order
	 * @return
	 */
	public Double apply(List<Meal> meals, Date order) {

		if (order.after(date_expires)) {
			return 0.;
		}

		if (freeShipping) {
			return -1.;
		}

		switch (discountType) {
			case MENU_PERCENT:
				if(meals.stream().anyMatch(m -> m.getCategory().equalsIgnoreCase("entree")) &&
						meals.stream().anyMatch(m -> m.getCategory().equalsIgnoreCase("plat")) &&
						meals.stream().anyMatch(m -> m.getCategory().equalsIgnoreCase("dessert"))) {
					return meals.stream().mapToDouble(Meal::getPrice).sum() * (this.amount/100);
				}
				break;
			case PERCENT:
				return meals.stream().mapToDouble(Meal::getPrice).sum() * (this.amount/100);
			case FIXED_CART:
				return Math.max(0., meals.stream().mapToDouble(Meal::getPrice).sum() - this.amount);
		}

		return 0.;
	}

	public enum DiscountType {
		/**
		 * A percentage discount for the selected meals
		 * For example 3 meals at 10€ = 30€, a coupon for 10% off
		 * applies a discount of 3€
		 */
		PERCENT,

		/**
		 * A fixed total discount for the meals
		 * For example 3 meals at 10€ = 30€, a coupon for 10€ off
		 * gives a discount of 10€
		 */
		FIXED_CART,

		/**
		 * A percentage discount for a menu
		 * Entrée - Plat - Dessert
		 */
		MENU_PERCENT
	}
}
