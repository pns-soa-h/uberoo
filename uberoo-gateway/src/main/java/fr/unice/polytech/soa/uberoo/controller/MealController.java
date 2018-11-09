package fr.unice.polytech.soa.uberoo.controller;

import fr.unice.polytech.soa.uberoo.assembler.MealResourceAssembler;
import fr.unice.polytech.soa.uberoo.exception.MealNotFoundException;
import fr.unice.polytech.soa.uberoo.exception.ServerException;
import fr.unice.polytech.soa.uberoo.model.Meal;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.mvc.TypeReferences;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
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
	private final RestTemplate restTemplate;

	@Autowired
	public MealController(MealResourceAssembler mealResourceAssembler) {
		this.mealResourceAssembler = mealResourceAssembler;
		this.restTemplate = new RestTemplate();
	}

	@GetMapping("/meals")
	public Resources<Resource<Meal>> getMeals(@RequestParam(value = "tag", required = false)String tag) {
		Traverson traverson = null;
		try {
			traverson = new Traverson(new URI("http://uberoo-meals:8080/"), MediaTypes.HAL_JSON);
			Traverson.TraversalBuilder tb = traverson.follow(rel("meals"));
			ParameterizedTypeReference<Resources<Resource<Meal>>> typeReference = new ParameterizedTypeReference<Resources<Resource<Meal>>>() {};
			Resources<Resource<Meal>> data = tb.toObject(typeReference);

			List<Resource<Meal>> req = new LinkedList<>();
			for (Resource<Meal> r : data) {
				req.add(mealResourceAssembler.toResource(r.getContent()));
			}

			return new Resources<>(req,
					linkTo(methodOn(MealController.class).getMeals(null)).withSelfRel());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;

	}

	@GetMapping("/meals/{id}")
	public Resource<Meal> getMeal(@PathVariable Long id) {
		ResponseEntity<Resource<Meal>> responseEntity = restTemplate.exchange("http://uberoo-meals:8080/meals/" + id,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<Resource<Meal>>() {},
				Collections.emptyMap());
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			return correctResource(responseEntity.getBody());
		} else if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
			throw new MealNotFoundException(id);
		} else {
			throw new ServerException(responseEntity);
		}
	}

	private Resource<Meal> correctResource(Resource<Meal> old) {
		return mealResourceAssembler.toResource(old.getContent());
	}
}
