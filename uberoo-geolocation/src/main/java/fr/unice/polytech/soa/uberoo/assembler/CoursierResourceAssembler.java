package fr.unice.polytech.soa.uberoo.assembler;

import fr.unice.polytech.soa.uberoo.controller.GeolocationController;
import fr.unice.polytech.soa.uberoo.model.Coursier;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Alexis Couvreur on 10/5/2018.
 */
@Component
public class CoursierResourceAssembler implements ResourceAssembler<Coursier, Resource<Coursier>> {

	@Override
	public Resource<Coursier> toResource(Coursier coursier) {

		return new Resource<>(coursier,
				linkTo(methodOn(GeolocationController.class).one(coursier.getId())).withSelfRel().expand(),
				linkTo(methodOn(GeolocationController.class).all(null)).withRel("coursiers").expand(),
				linkTo(methodOn(GeolocationController.class).location(coursier.getId())).withRel("coursiers").expand()
		);
	}
}
