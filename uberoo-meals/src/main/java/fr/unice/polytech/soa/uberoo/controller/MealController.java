package fr.unice.polytech.soa.uberoo.controller;

import fr.unice.polytech.soa.uberoo.assembler.MealResourceAssembler;
import fr.unice.polytech.soa.uberoo.exception.MealNotFoundException;
import fr.unice.polytech.soa.uberoo.model.Meal;
import fr.unice.polytech.soa.uberoo.repository.MealRepository;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Defines exposed method on meals API.
 *
 * @author Julien Lemaire
 */
@RestController
public class MealController {

	private final MealRepository repository;

	private final MealResourceAssembler resourceAssembler;

	public MealController(MealRepository repository, MealResourceAssembler resourceAssembler) {
		this.repository = repository;
		this.resourceAssembler = resourceAssembler;
	}

	@GetMapping("/meals")
	public Resources<Resource<Meal>> getMeals(@RequestParam(value = "tag", required = false)String tag) {

		List<Resource<Meal>> meals = repository.findAll().stream()
				.filter(m -> tag == null || m.getTag().getLabel().equals(tag))
				.map(resourceAssembler::toResource)
				.collect(Collectors.toList());

		return new Resources<>(meals,
				linkTo(methodOn(MealController.class).getMeals(null)).withSelfRel());
	}

	@GetMapping("/meals/{id}")
	public Resource<Meal> getMeal(@PathVariable Long id) {
		Meal meal = repository.findById(id).orElseThrow(() -> new MealNotFoundException(id));

		return resourceAssembler.toResource(meal);
	}
}
