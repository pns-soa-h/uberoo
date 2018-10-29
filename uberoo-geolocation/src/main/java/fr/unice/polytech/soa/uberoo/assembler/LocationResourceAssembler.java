package fr.unice.polytech.soa.uberoo.assembler;

import fr.unice.polytech.soa.uberoo.controller.GeolocationController;
import fr.unice.polytech.soa.uberoo.model.Coursier;
import fr.unice.polytech.soa.uberoo.model.Location;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Alexis Couvreur on 10/5/2018.
 */
@Component
public class LocationResourceAssembler implements ResourceAssembler<Location, Resource<Location>> {

	@Override
	public Resource<Location> toResource(Location location) {

		return new Resource<>(location,
				linkTo(methodOn(GeolocationController.class).location(location.getPlace())).withSelfRel().expand()
		);
	}
}
