package fr.unice.polytech.soa.uberoo.controller;

import fr.unice.polytech.soa.uberoo.exception.MalformedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Alexis Couvreur on 10/5/2018.
 */
@ControllerAdvice
public class MalformedAdvice {
	@ResponseBody
	@ExceptionHandler(MalformedException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	String orderNotFoundHandler(MalformedException ex) {
		return ex.getMessage();
	}
}
