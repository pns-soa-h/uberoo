package fr.unice.polytech.soa.uberoo.assembler;

import fr.unice.polytech.soa.uberoo.controller.orders.OrderController;
import fr.unice.polytech.soa.uberoo.model.orders.Order;
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

		return new Resource<>(order,
				linkTo(methodOn(OrderController.class).one(order.getOrderId())).withSelfRel(),
				linkTo(methodOn(OrderController.class).all(null, null)).withRel("orders"),
				linkTo(methodOn(OrderController.class).updateStatus(order.getOrderId(), null)).withRel("update")
		);
	}
}
