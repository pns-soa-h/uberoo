package fr.unice.polytech.soa.uberoo.controller;

import fr.unice.polytech.soa.uberoo.assembler.MealResourceAssembler;
import fr.unice.polytech.soa.uberoo.exception.MealNotFoundException;
import fr.unice.polytech.soa.uberoo.exception.ServerException;
import fr.unice.polytech.soa.uberoo.model.Meal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Defines exposed method on meals API.
 *
 * @author Julien Lemaire
 */
@RestController
public class MealController {

	private final MealResourceAssembler mealResourceAssembler;

	@Autowired
	public MealController(MealResourceAssembler mealResourceAssembler) {
		this.mealResourceAssembler = mealResourceAssembler;
	}

	@GetMapping("/meals")
	public Resources<Resource<Meal>> getMeals(@RequestParam(value = "tag", required = false)String tag) {
		return null;
	}

	@GetMapping("/meals/{id}")
	public Resource<Meal> getMeal(@PathVariable Long id) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Resource<Meal>> responseEntity = restTemplate.exchange("http://uberoo-meals:8080/meals/" + id,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<Resource<Meal>>() {},
				Collections.emptyMap());
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			Meal meal = responseEntity.getBody().getContent();
			return mealResourceAssembler.toResource(meal);
		} else if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
			throw new MealNotFoundException(id);
		} else {
			throw new ServerException(responseEntity);
		}
	}
}
