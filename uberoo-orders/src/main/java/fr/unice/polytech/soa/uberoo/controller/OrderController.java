package fr.unice.polytech.soa.uberoo.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import fr.unice.polytech.soa.uberoo.TimeETA;
import fr.unice.polytech.soa.uberoo.TimeETAMock;
import fr.unice.polytech.soa.uberoo.assembler.OrderResourceAssembler;
import fr.unice.polytech.soa.uberoo.exception.MalformedException;
import fr.unice.polytech.soa.uberoo.exception.OrderNotFoundException;
import fr.unice.polytech.soa.uberoo.exception.BodyMemberNotFoundException;
import fr.unice.polytech.soa.uberoo.model.Order;
import fr.unice.polytech.soa.uberoo.model.OrderRequest;
import fr.unice.polytech.soa.uberoo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class OrderController {

    private final OrderRepository repository;
	private final OrderResourceAssembler assembler;
	private TimeETA timeETA;

    @Autowired
    public OrderController(OrderRepository repository, OrderResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
        this.timeETA = new TimeETAMock();
    }

	@GetMapping("/orders")
	public Resources<Resource<Order>> all(
			@RequestParam(value = "status", required = false)Order.Status status,
			@RequestParam(value = "restaurant", required = false) Long restaurantId) {

		List<Resource<Order>> orders = repository.findAll().stream()
				.filter(order -> status == null || order.getStatus() == status)
				.filter(order -> restaurantId == null || order.getRestaurant().getId().equals(restaurantId))
				.map(assembler::toResource)
				.collect(Collectors.toList());

		return new Resources<>(orders,
				linkTo(methodOn(OrderController.class).all(status, restaurantId)).withSelfRel());
	}

	@GetMapping("/orders/{id}")
	public Resource<Order> one(@PathVariable Long id) {
		return assembler.toResource(
				repository.findById(id)
						.orElseThrow(() -> new OrderNotFoundException(id)));
	}

	@PostMapping(value = "/orders", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Resource<Order>> newOrder(@RequestBody OrderRequest orderRequest) {

		System.out.println(orderRequest);
    	Order order = new Order(orderRequest);
		order.setStatus(Order.Status.IN_PROGRESS);
		order.setEta(timeETA.calculateOrderETA(order, null));

		Order newOrder = repository.save(order);

		return ResponseEntity
				.created(linkTo(methodOn(OrderController.class).one(newOrder.getId())).toUri())
				.body(assembler.toResource(newOrder));
	}


	@PatchMapping("/orders/{id}/status")
	public ResponseEntity<ResourceSupport> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> map) {

    	String strStatus = map.get("status");
    	if(strStatus == null) {
			throw new BodyMemberNotFoundException("status");
		}

		Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
		Order.Status status = Order.Status.valueOf(strStatus);

		// If you're cancelling an order that is not cancellable anymore
		if(status.equals(Order.Status.CANCELLED) && !order.getStatus().equals(Order.Status.IN_PROGRESS)) {
			return ResponseEntity
					.status(HttpStatus.METHOD_NOT_ALLOWED)
					.body(new VndErrors.VndError("Method not allowed", "(1) You can't set an order that is " + order.getStatus().toString() + " into " + status.toString()));
		}

		// If you're not updating the status to the next step
		if(!status.equals(Order.Status.CANCELLED) && !order.getStatus().next().equals(status)) {
			return ResponseEntity
					.status(HttpStatus.METHOD_NOT_ALLOWED)
					.body(new VndErrors.VndError("Method not allowed", "(2) You can't set an order that is " + order.getStatus().toString() + " into " + status.toString()));
		}


		// Special case for delivery, body contains coursier id
		// /!\ DEPRECATED /!\
		if (order.getStatus() == Order.Status.IN_PREPARATION) {
			order.setStatus(Order.Status.IN_TRANSIT);

			// Retrieve coursierId from body and check if present
			String coursierId = map.get("coursierId");
			if (coursierId == null) {
				// else 422
				throw new BodyMemberNotFoundException("coursierId");
			}

			// Check if header is well formed
			try {
				order.setCoursierId(Long.parseLong(coursierId));
			} catch (NumberFormatException ex) {
				// Else 422 ...
				throw new MalformedException("coursierId", "number");
			}
		}

		order.setStatus(status);

		return ResponseEntity.ok(assembler.toResource(repository.save(order)));

	}
}
