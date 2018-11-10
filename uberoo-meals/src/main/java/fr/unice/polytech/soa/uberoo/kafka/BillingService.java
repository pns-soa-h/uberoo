package fr.unice.polytech.soa.uberoo.kafka;

import fr.unice.polytech.soa.uberoo.model.Bill;
import fr.unice.polytech.soa.uberoo.model.Coupon;
import fr.unice.polytech.soa.uberoo.model.Meal;
import fr.unice.polytech.soa.uberoo.model.Order;
import fr.unice.polytech.soa.uberoo.repository.CouponRepository;
import fr.unice.polytech.soa.uberoo.repository.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
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
	@SendTo
	public Bill onOrderRequestBill(Order order) {
		Bill bill = new Bill();
		List<Meal> meals = new ArrayList<>();
		Double total = 0.;

		// Meal
		for (Long id : order.getMeals()) {
			Optional<Meal> meal = mealRepository.findById(id);
			meal.ifPresent(meals::add);
		}

		bill.setSubTotal(total);

		total += 1; // Mock for delivery cost 1€

		// Coupon
		Optional<Coupon> coupon = couponRepository.findByCode(order.getCoupon().getCode());
		if (coupon.isPresent()) {
			total -= coupon.get().apply(meals, order.getCreatedAt());
		}

		bill.setTotal(total);

		return bill;
	}
}
