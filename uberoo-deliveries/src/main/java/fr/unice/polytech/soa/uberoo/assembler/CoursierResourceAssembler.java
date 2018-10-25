package fr.unice.polytech.soa.uberoo.assembler;

import fr.unice.polytech.soa.uberoo.controller.CoursierController;
import fr.unice.polytech.soa.uberoo.model.Coursier;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 *
 * @author Julien Lemaire
 */
@Component
public class CoursierResourceAssembler implements ResourceAssembler<Coursier, Resource<Coursier>> {

	@Override
	public Resource<Coursier> toResource(Coursier coursier) {
		return new Resource<>(coursier,
				linkTo(methodOn(CoursierController.class).one(coursier.getId())).withSelfRel(),
				linkTo(methodOn(CoursierController.class).all(null)).withRel("coursiers"));
	}
}
