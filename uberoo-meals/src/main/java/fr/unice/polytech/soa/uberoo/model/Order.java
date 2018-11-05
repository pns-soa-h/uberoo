package fr.unice.polytech.soa.uberoo.model;

import java.sql.Date;

public class Order {

	private Date createdAt;
	private Restaurant restaurant;
	private MealWithQuantity meal;
	private Coupon coupon;

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public MealWithQuantity getMeal() {
		return meal;
	}

	public void setMeal(MealWithQuantity meal) {
		this.meal = meal;
	}

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public class MealWithQuantity extends Meal {
		private Integer quantity;

		public MealWithQuantity() {
			super();
		}

		public Integer getQuantity() {
			return quantity;
		}

		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}
	}
}
