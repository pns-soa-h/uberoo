package fr.unice.polytech.soa.uberoo.controller.orders;

import fr.unice.polytech.soa.uberoo.exception.BodyMemberNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Alexis Couvreur on 10/5/2018.
 */

@ControllerAdvice
public class BodyMemberNotFoundAdvice {
	@ResponseBody
	@ExceptionHandler(BodyMemberNotFoundException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	String orderNotFoundHandler(BodyMemberNotFoundException ex) {
		return ex.getMessage();
	}
}
