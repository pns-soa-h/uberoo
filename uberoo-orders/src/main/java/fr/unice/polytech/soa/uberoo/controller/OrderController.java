package fr.unice.polytech.soa.uberoo.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import fr.unice.polytech.soa.uberoo.TimeETA;
import fr.unice.polytech.soa.uberoo.TimeETAMock;
import fr.unice.polytech.soa.uberoo.assembler.OrderResourceAssembler;
import fr.unice.polytech.soa.uberoo.exception.MalformedException;
import fr.unice.polytech.soa.uberoo.exception.OrderNotFoundException;
import fr.unice.polytech.soa.uberoo.exception.BodyMemberNotFoundException;
import fr.unice.polytech.soa.uberoo.model.Order;
import fr.unice.polytech.soa.uberoo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class OrderController {

    private final OrderRepository repository;
	private final OrderResourceAssembler assembler;
	private TimeETA timeETA;

	private KafkaTemplate<String, Order> kafkaTemplate;


	@Autowired
    public OrderController(OrderRepository repository, OrderResourceAssembler assembler, KafkaTemplate<String, Order> kafkaTemplate) {
        this.repository = repository;
        this.assembler = assembler;
        this.kafkaTemplate = kafkaTemplate;
        this.timeETA = new TimeETAMock();
    }

	@GetMapping("/orders")
	public Resources<Resource<Order>> all(
			@RequestParam(value = "status", required = false)Order.Status status,
			@RequestParam(value = "restaurant", required = false) Long restaurantId) {

		List<Resource<Order>> orders = repository.findAll().stream()
				.filter(order -> status == null || order.getStatus() == status)
				.filter(order -> restaurantId == null || order.getRestaurantId().equals(restaurantId))
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

	@PostMapping("/orders")
	public ResponseEntity<Resource<Order>> newOrder(@RequestBody Order order) {

		order.setStatus(Order.Status.IN_PROGRESS);
		order.setETA(timeETA.calculateOrderETA(order, null));

		Order newOrder = repository.save(order);

		return ResponseEntity
				.created(linkTo(methodOn(OrderController.class).one(newOrder.getId())).toUri())
				.body(assembler.toResource(newOrder));
	}


	@PutMapping("/orders/{id}/complete")
	public ResponseEntity<ResourceSupport> complete(@PathVariable Long id) {

		Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

		if (order.getStatus() == Order.Status.IN_PROGRESS) {
			order.setStatus(Order.Status.COMPLETED);
			kafkaTemplate.send("order", order);
			return ResponseEntity.ok(assembler.toResource(repository.save(order)));
		}

		return ResponseEntity
				.status(HttpStatus.METHOD_NOT_ALLOWED)
				.body(new VndErrors.VndError("Method not allowed", "You can't complete an order that is in the " + order.getStatus() + " status"));
	}

	@DeleteMapping("/orders/{id}/cancel")
	public ResponseEntity<ResourceSupport> cancel(@PathVariable Long id) {

		Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

		if (order.getStatus() == Order.Status.IN_PROGRESS) {
			order.setStatus(Order.Status.CANCELLED);
			return ResponseEntity.ok(assembler.toResource(repository.save(order)));
		}

		return ResponseEntity
				.status(HttpStatus.METHOD_NOT_ALLOWED)
				.body(new VndErrors.VndError("Method not allowed", "You can't cancel an order that is in the " + order.getStatus() + " status"));
	}


    @PutMapping(value = "/orders/{id}/assign")
    public ResponseEntity<ResourceSupport> assign(@PathVariable("id") Long id, @RequestBody() Map<String, String> body) {

		Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

		if (order.getStatus() == Order.Status.COMPLETED) {
			order.setStatus(Order.Status.ASSIGNED);

			// Retrieve coursierId from body and check if present
			String coursierId = body.get("coursierId");
			if(coursierId == null) {
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
			return ResponseEntity.ok(assembler.toResource(repository.save(order)));
		}

		return ResponseEntity
				.status(HttpStatus.METHOD_NOT_ALLOWED)
				.body(new VndErrors.VndError("Method not allowed", "You can't complete an order that is in the " + order.getStatus() + " status"));
    }
}
