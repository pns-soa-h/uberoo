package fr.unice.polytech.soa.uberoo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.unice.polytech.soa.uberoo.exception.OrderNotFoundException;
import fr.unice.polytech.soa.uberoo.model.Order;
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
	
	@GetMapping("/payments/{id}")
	public void payOrder(@PathVariable Long id) {
		Order o = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
	}
	
	
	

}
