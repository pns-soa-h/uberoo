package fr.unice.polytech.soa.uberoo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Picard Marchetto Ivan on 10/1/2018.
 */
@RestController
public interface TimeETA {

	@GetMapping("/timeETA/{idClient}/{idCoursier}/{idMeal}")
	long calculateOrderETA(@PathVariable("idClient") long idClient,@PathVariable("idCoursier") long idCoursier,
			@PathVariable("idMeal") long idMeal);
	
	
}
