package fr.unice.polytech.soa.uberoo.assembler;

import fr.unice.polytech.soa.uberoo.controller.MealController;
import fr.unice.polytech.soa.uberoo.model.Meal;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Assembling Meals as resources.
 *
 * @author Julien Lemaire
 */
@Component
public class MealResourceAssembler implements ResourceAssembler<Meal, Resource<Meal>> {
	@Override
	public Resource<Meal> toResource(Meal meal) {
		return new Resource<>(meal,
				linkTo(methodOn(MealController.class).getMeal(meal.getMealId())).withSelfRel(),
				linkTo(methodOn(MealController.class).getMeals(null)).withRel("meals"));
	}
}
