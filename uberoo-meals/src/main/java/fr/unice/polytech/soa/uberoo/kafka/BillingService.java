package fr.unice.polytech.soa.uberoo.kafka;

import com.google.gson.Gson;
import fr.unice.polytech.soa.uberoo.model.Bill;
import fr.unice.polytech.soa.uberoo.model.Coupon;
import fr.unice.polytech.soa.uberoo.model.Meal;
import fr.unice.polytech.soa.uberoo.model.Order;
import fr.unice.polytech.soa.uberoo.repository.CouponRepository;
import fr.unice.polytech.soa.uberoo.repository.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BillingService {

	private final MealRepository mealRepository;
	private final CouponRepository couponRepository;

	@Autowired
	public BillingService(MealRepository mealRepository, CouponRepository couponRepository) {
		this.mealRepository = mealRepository;
		this.couponRepository = couponRepository;
	}

	@KafkaListener(topics = "order_bill")
	@SendTo("order_total_computed")
	public String onOrderRequestBill(String orderS, Acknowledgment acknowledgment) {

		Gson gson = new Gson();

		orderS = orderS.replace("\\", "");
		orderS = orderS.substring(1, orderS.length() - 1);
		// 1. JSON to Java object, read it from a file.
		Order order = gson.fromJson(orderS, Order.class);
		Bill bill = new Bill();
		List<Meal> meals = new ArrayList<>();
		Double total = 0.;

		// Meal
		for (Meal m : order.getMeals()) {
			Optional<Meal> meal = mealRepository.findById(m.getId());
			meal.ifPresent(meals::add);
		}

		total = meals.stream().mapToDouble(Meal::getPrice).sum();

		bill.setShippingPrice(1.);
		total += 1; // Mock for delivery cost 1€
		bill.setSubTotal(total);

		// Coupon
		if (order.getCoupon() != null) {
			Optional<Coupon> coupon = couponRepository.findByCode(order.getCoupon().getCode());
			if (coupon.isPresent()) {
				total -= coupon.get().apply(meals, order.getCreatedAt());
				bill.setCoupon(coupon.get());
			}
		}
		bill.setTotal(total);

		acknowledgment.acknowledge();
		return gson.toJson(bill);
	}
}
