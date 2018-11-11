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

		return new Resource<>(order,
				linkTo(methodOn(OrderController.class).one(order.getId())).withSelfRel().expand(),
				linkTo(methodOn(OrderController.class).all(null, null)).withRel("orders").expand(),
				linkTo(methodOn(OrderController.class).updateStatus(order.getId(), null)).withRel("update")
		);
	}
}
