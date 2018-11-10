package fr.unice.polytech.soa.uberoo.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import fr.unice.polytech.soa.uberoo.TimeETA;
import fr.unice.polytech.soa.uberoo.TimeETAMock;
import fr.unice.polytech.soa.uberoo.assembler.OrderResourceAssembler;
import fr.unice.polytech.soa.uberoo.exception.MalformedException;
import fr.unice.polytech.soa.uberoo.exception.OrderNotFoundException;
import fr.unice.polytech.soa.uberoo.exception.BodyMemberNotFoundException;
import fr.unice.polytech.soa.uberoo.model.Bill;
import fr.unice.polytech.soa.uberoo.model.Order;
import fr.unice.polytech.soa.uberoo.repository.OrderRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerErrorException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
public class OrderController {

    private final OrderRepository repository;
	private final OrderResourceAssembler assembler;

	private TimeETA timeETA;

	private KafkaTemplate<String, Order> kafkaTemplate;
	private ReplyingKafkaTemplate<String, Order, Bill> replyingKafkaTemplate;

	@Autowired
    public OrderController(OrderRepository repository, OrderResourceAssembler assembler, KafkaTemplate<String, Order> kafkaTemplate, ReplyingKafkaTemplate<String, Order, Bill> replyingKafkaTemplate) {
        this.repository = repository;
        this.assembler = assembler;
        this.kafkaTemplate = kafkaTemplate;
        this.timeETA = new TimeETAMock();
        this.replyingKafkaTemplate = replyingKafkaTemplate;
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
	public ResponseEntity<Resource<Order>> newOrder(@RequestBody Order order) {

		order.setStatus(Order.Status.IN_PROGRESS);
		order.setEta(timeETA.calculateOrderETA(order, null));

		// Ask for total
		// kafkaTemplate.send("order_need_total", order);
		/*RequestReplyFuture<String, Order, Bill> replyFuture = replyingKafkaTemplate.sendAndReceive(new ProducerRecord<>("order_bill", order));
		ConsumerRecord<String, Bill> sendResult = null;
		try {
			sendResult = replyFuture.get();
		} catch (InterruptedException e) {
			// error
			throw new ServerErrorException("InterruptedException sending kafka message", e);
		} catch (ExecutionException e) {
			// cancelled
			throw new ServerErrorException("ExecutionException (cancelled) sending kafka message", e);
		}
		Bill bill = sendResult.value();

		order.setTotal(bill.getTotal());
		order.setSubtotal(bill.getSubTotal());
		order.setTotalShipping(bill.getShippingPrice());

		// Check if the order has a coupon
		// and if the bill returned has a valid used coupon
		if(order.getCoupon() != null && bill.getCoupon() != null) {
			order.getCoupon().setDescription(bill.getCoupon().getDescription());
		}
*/
		Order newOrder = repository.save(order);

		return ResponseEntity
				.created(linkTo(methodOn(OrderController.class).one(newOrder.getId())).toUri())
				.body(assembler.toResource(newOrder));
	}


	@PatchMapping(value = "/orders/{id}/status", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<ResourceSupport> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> map) {

    	String strStatus = map.get("status");
    	if(strStatus == null) {
			throw new BodyMemberNotFoundException("status");
		}

		Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
		Order.Status status;
		try {
			status = Order.Status.valueOf(strStatus);
		} catch (IllegalArgumentException e) {
			throw new MalformedException(strStatus, Arrays.toString(Order.Status.values()));
		}

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

		if(status == Order.Status.ACCEPTED) {
			String paymentMethod = map.get("payment_method");
			if(paymentMethod == null) {
				throw new BodyMemberNotFoundException("payment_method");
			}

			if(paymentMethod.equals("cb")) {
				// Send msg pay this
			}
		}
		switch (status) {
			case ACCEPTED:
				kafkaTemplate.send("order_bill", order);
				break;
		}

		order.setStatus(status);
		// kafkaTemplate.send("order_status_" + status.name().toLowerCase(), order);
		return ResponseEntity.ok(assembler.toResource(repository.save(order)));
	}
}
