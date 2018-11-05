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
		Double total = 0.;

		// Meal
		Optional<Meal> meal = mealRepository.findById(order.getMeal().getId());
		if (meal.isPresent()) {
			total += meal.get().getPrice() * order.getMeal().getQuantity();
		} else {
			// Error meal does not exist
			return bill;
		}
		bill.setSubTotal(total);

		total += 1; // Mock for delivery cost 1€

		// Coupon
		Optional<Coupon> coupon = couponRepository.findByCode(order.getCoupon().getCode());
		if (coupon.isPresent()) {
			// If the coupon is applicable to the restaurant
			if (coupon.get().getRestaurantId().equals(meal.get().getRestaurant().getId())) {
				total = coupon.get().apply(total, order.getCreatedAt());
			}
		}

		bill.setTotal(total);

		return bill;
	}
}
