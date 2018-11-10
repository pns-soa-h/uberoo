package fr.unice.polytech.soa.uberoo.controller;

import fr.unice.polytech.soa.uberoo.assembler.MealResourceAssembler;
import fr.unice.polytech.soa.uberoo.exception.MealNotFoundException;
import fr.unice.polytech.soa.uberoo.exception.ServerException;
import fr.unice.polytech.soa.uberoo.model.Meal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.springframework.hateoas.client.Hop.rel;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Defines exposed method on meals API.
 *
 * @author Julien Lemaire
 */
@RestController
public class MealController {

	private final MealResourceAssembler mealResourceAssembler;
	// HATEOAS client
	private final Traverson traverson;

	@Autowired
	public MealController(MealResourceAssembler mealResourceAssembler) throws URISyntaxException {
		this.mealResourceAssembler = mealResourceAssembler;
		this.traverson = new Traverson(new URI("http://uberoo-meals:8080/"), MediaTypes.HAL_JSON);
	}

	@GetMapping("/meals")
	public Resources<Resource<Meal>> getMeals(@RequestParam(value = "tag", required = false)String tag) {
		Traverson.TraversalBuilder tb = traverson.follow(rel("meals"))
			.follow(rel("self").withParameter("tag", tag));
		ParameterizedTypeReference<Resources<Resource<Meal>>> typeReference = new ParameterizedTypeReference<Resources<Resource<Meal>>>() {};
		Resources<Resource<Meal>> data = tb.toObject(typeReference);

		List<Resource<Meal>> req = new LinkedList<>();
		for (Resource<Meal> r : data) {
			req.add(mealResourceAssembler.toResource(r.getContent()));
		}

		return new Resources<>(req,
				linkTo(methodOn(MealController.class).getMeals(null)).withSelfRel(),
				linkTo(methodOn(MealController.class).getMeal(null)).withRel("item"));
	}

	@GetMapping("/meals/{id}")
	public Resource<Meal> getMeal(@PathVariable Long id) {
		Traverson.TraversalBuilder tb = traverson.follow(rel("meals")).follow(rel("item").withParameter("id", id));
		ParameterizedTypeReference<Resource<Meal>> typeReference = new ParameterizedTypeReference<Resource<Meal>>() {};
		return mealResourceAssembler.toResource(tb.toObject(typeReference).getContent());
	}

	private Resource<Meal> correctResource(Resource<Meal> old) {
		return mealResourceAssembler.toResource(old.getContent());
	}
}
