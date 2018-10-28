package fr.unice.polytech.soa.uberoo.assembler;

import fr.unice.polytech.soa.uberoo.controller.OrderController;
import fr.unice.polytech.soa.uberoo.model.Order;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Alexis Couvreur on 10/5/2018.
 */
@Component
public class OrderResourceAssembler implements ResourceAssembler<Order, Resource<Order>> {

	@Override
	public Resource<Order> toResource(Order order) {

		// Unconditional links to single-item resource and aggregate root
		Resource<Order> orderResource = new Resource<>(order,
				linkTo(methodOn(OrderController.class).one(order.getId())).withSelfRel().expand(),
				linkTo(methodOn(OrderController.class).all()).withRel("orders").expand()
		);
		return orderResource;
	}
}