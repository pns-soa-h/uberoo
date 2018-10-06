package fr.unice.polytech.soa.uberoo.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import fr.unice.polytech.soa.uberoo.TimeETA;
import fr.unice.polytech.soa.uberoo.TimeETAMock;
import fr.unice.polytech.soa.uberoo.controller.OrderController;
import fr.unice.polytech.soa.uberoo.model.Order;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexis Couvreur on 10/5/2018.
 */
@Component
public class OrderResourceAssembler implements ResourceAssembler<Order, Resource<Order>> {

	@Override
	public Resource<Order> toResource(Order order) {

		// Unconditional links to single-item resource and aggregate root

		Resource<Order> orderResource = new Resource<>(order,
				linkTo(methodOn(OrderController.class).one(order.getId())).withSelfRel(),
				linkTo(methodOn(OrderController.class).all()).withRel("orders")
		);

		// Conditional links based on state of the order

		if (order.getStatus() == Order.Status.IN_PROGRESS) {

			orderResource.add(
					linkTo(methodOn(OrderController.class)
							.cancel(order.getId())).withRel("cancel"));
			orderResource.add(
					linkTo(methodOn(OrderController.class)
							.complete(order.getId())).withRel("complete"));
		}

		if (order.getStatus() == Order.Status.COMPLETED) {
			orderResource.add(
					linkTo(methodOn(OrderController.class)
							.assign(order.getId(), new HashMap<>())).withRel("assign"));
		}

		return orderResource;
	}
}
