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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class OrderController {

    private final OrderRepository repository;
	private final OrderResourceAssembler assembler;

	private final CoursierRepository coursierRepository;

	@Autowired
    public OrderController(OrderRepository repository, OrderResourceAssembler assembler, CoursierRepository coursierRepository) {
        this.repository = repository;
        this.assembler = assembler;
        this.coursierRepository = coursierRepository;
    }

	@GetMapping("/orders")
	public Resources<Resource<Order>> all() {

		List<Resource<Order>> orders = repository.findAll().stream()
				.map(assembler::toResource)
				.collect(Collectors.toList());

		return new Resources<>(orders,
				linkTo(methodOn(OrderController.class).all()).withSelfRel());
	}

	@GetMapping("/orders/{id}")
	public Resource<Order> one(@PathVariable Long id) {
		return assembler.toResource(
				repository.findById(id)
						.orElseThrow(() -> new OrderNotFoundException(id)));

	}

	@PatchMapping("/orders/{id}")
	public @ResponseBody ResponseEntity<Resource<Order>> assignCoursier(@PathVariable Long id, @RequestBody Coursier coursier) {
		Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
		Coursier coursierLoaded = coursierRepository.findById(coursier.getId()).orElseThrow(() -> new CoursierNotFoundException(coursier.getId()));
		order.setCoursier(coursierLoaded);
		Order res = repository.save(order);
		System.out.println("TEEEST " + res.getCoursier());
		return ResponseEntity.ok(assembler.toResource(res));
	}
}
