package fr.unice.polytech.soa.uberoo.assembler;

import fr.unice.polytech.soa.uberoo.model.Coupon;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class CouponResourceAssemler implements ResourceAssembler<Coupon, Resource<Coupon>> {
	@Override
	public Resource<Coupon> toResource(Coupon entity) {
		return new Resource<>(entity/*,
				linkTo(methodOn(CouponController.class).getMeal(meal.getId())).withSelfRel(),
				linkTo(methodOn(CouponController.class).getMeals(null)).withRel("meals")*/);
	}
}
