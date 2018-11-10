package fr.unice.polytech.soa.uberoo.model;

import java.sql.Date;
import java.util.List;

public class Order {

	private Date createdAt;
	private Restaurant restaurant;
	private List<Long> meals;
	private Coupon coupon;

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
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

	public List<Long> getMeals() {
		return meals;
	}

	public void setMeals(List<Long> meals) {
		this.meals = meals;
	}
}
