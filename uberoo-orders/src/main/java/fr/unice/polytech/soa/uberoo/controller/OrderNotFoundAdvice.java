package fr.unice.polytech.soa.uberoo.controller;

import fr.unice.polytech.soa.uberoo.exception.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Alexis Couvreur on 10/5/2018.
 */
@ControllerAdvice
public class OrderNotFoundAdvice {
	@ResponseBody
	@ExceptionHandler(OrderNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String orderNotFoundHandler(OrderNotFoundException ex) {
		return ex.getMessage();
	}
}
