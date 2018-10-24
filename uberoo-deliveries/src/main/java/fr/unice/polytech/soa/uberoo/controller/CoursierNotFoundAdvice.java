package fr.unice.polytech.soa.uberoo.controller;

import fr.unice.polytech.soa.uberoo.exception.CoursierNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CoursierNotFoundAdvice {
	@ResponseBody
	@ExceptionHandler(CoursierNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String orderNotFoundHandler(CoursierNotFoundException ex) {
		return ex.getMessage();
	}
}
