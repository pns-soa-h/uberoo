package fr.unice.polytech.soa.uberoo.assembler;

import fr.unice.polytech.soa.uberoo.controller.CouponController;
import fr.unice.polytech.soa.uberoo.model.Coupon;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class CouponResourceAssemler implements ResourceAssembler<Coupon, Resource<Coupon>> {
	@Override
	public Resource<Coupon> toResource(Coupon entity) {
		return new Resource<>(entity,
				linkTo(methodOn(CouponController.class).findAll(null)).withRel("coupons"));
	}
}
