package fr.unice.polytech.soa.uberoo.controller;

import fr.unice.polytech.soa.uberoo.model.Meal;
import fr.unice.polytech.soa.uberoo.repository.MealRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Defines exposed method on API.
 *
 * @author Julien Lemaire
 */
@RestController
public class MealController {

	private final MealRepository repository;

	public MealController(MealRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/meals")
	public List<Meal> getMeals() {
		return repository.findAll();
	}

}
