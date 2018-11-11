package fr.unice.polytech.soa.uberoo.controller;

import fr.unice.polytech.soa.uberoo.assembler.OrderResourceAssembler;
import fr.unice.polytech.soa.uberoo.exception.CoursierNotFoundException;
import fr.unice.polytech.soa.uberoo.exception.OrderNotFoundException;
import fr.unice.polytech.soa.uberoo.model.Coursier;
import fr.unice.polytech.soa.uberoo.model.Order;
import fr.unice.polytech.soa.uberoo.repository.CoursierRepository;
import fr.unice.polytech.soa.uberoo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class OrderController {

	private final OrderRepository orderRepository;
	private final OrderResourceAssembler assembler;
	private final CoursierRepository coursierRepository;

	private KafkaTemplate <String, Long> kafkaTemplate;

	@Autowired
	public OrderController(OrderRepository orderRepository, OrderResourceAssembler assembler, CoursierRepository coursierRepository, KafkaTemplate <String, Long> kafkaTemplate) {
		this.orderRepository = orderRepository;
		this.assembler = assembler;
		this.coursierRepository = coursierRepository;
		this.kafkaTemplate = kafkaTemplate;
	}

	@GetMapping("/orders")
	public Resources <Resource <Order>> all() {

		List <Resource <Order>> orders = orderRepository.findAll().stream()
				.map(assembler::toResource)
				.collect(Collectors.toList());

		return new Resources <>(orders,
				linkTo(methodOn(OrderController.class).all()).withSelfRel());
	}

	@GetMapping("/orders/{id}")
	public Resource <Order> one(@PathVariable Long id) {
		return assembler.toResource(
				orderRepository.findById(id)
						.orElseThrow(() -> new OrderNotFoundException(id)));

	}

	@PatchMapping("/orders/{id}")
	public @ResponseBody
	ResponseEntity <Resource <Order>> assignCoursier(@PathVariable Long id, @RequestBody Coursier coursier) {
		Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
		Coursier coursierLoaded = coursierRepository.findById(coursier.getId()).orElseThrow(() -> new CoursierNotFoundException(coursier.getId()));
		order.setCoursier(coursierLoaded);
		Order res = orderRepository.save(order);
		System.out.println("TEEEST " + res.getCoursier());
		return ResponseEntity.ok(assembler.toResource(res));
	}

	@PatchMapping(value = "/orders/{id}/status", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody
	ResponseEntity <Resource <Order>> changeStatus(@PathVariable Long id, @RequestBody Map <String, String> map) {
		Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
		order.setStatus(Order.Status.valueOf(map.get("status")));
		Order res = orderRepository.save(order);
		Coursier coursier = order.getCoursier();
		coursier.increaseAmount(10);
		coursierRepository.save(coursier);
		kafkaTemplate.send("orderDelivered", order.getId());
		return ResponseEntity.ok(assembler.toResource(res));
	}

	@KafkaListener(topics = "order")
	public void deliver(Order order, Acknowledgment acknowledgment) {
		order.setStatus(Order.Status.IN_PROGRESS);
		orderRepository.save(order);
		acknowledgment.acknowledge();
	}
}
