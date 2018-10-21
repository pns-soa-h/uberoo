package fr.unice.polytech.soa.uberoo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import fr.unice.polytech.soa.uberoo.repository.OrderRepository;
import fr.unice.polytech.soa.uberoo.repository.RestaurantRepository;

@RestController
public class PaymentController {
	
	private final OrderRepository orderRepository;
	private final RestaurantRepository restaurantRepository;
	
	@Autowired
	public PaymentController(OrderRepository orderRepository, RestaurantRepository restaurantRepository) {
		this.orderRepository = orderRepository;
		this.restaurantRepository = restaurantRepository;
	}
	
	
	
	

}
