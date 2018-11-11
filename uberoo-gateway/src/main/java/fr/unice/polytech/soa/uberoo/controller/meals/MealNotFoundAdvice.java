package fr.unice.polytech.soa.uberoo.controller.meals;

import fr.unice.polytech.soa.uberoo.exception.MealNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Describes behavior to adopt if a meal isn't found.
 *
 * @author Julien Lemaire
 */
@ControllerAdvice
public class MealNotFoundAdvice {

	@ResponseBody
	@ExceptionHandler(MealNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String mealNotFoundHandler(MealNotFoundException ex) {
		return ex.getMessage();
	}
}
