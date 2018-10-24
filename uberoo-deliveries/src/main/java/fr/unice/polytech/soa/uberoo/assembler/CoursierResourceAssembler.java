package fr.unice.polytech.soa.uberoo.assembler;

import fr.unice.polytech.soa.uberoo.model.Coursier;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 *
 * @author Julien Lemaire
 */
@Component
public class CoursierResourceAssembler implements ResourceAssembler<Coursier, Resource<Coursier>> {

	@Override
	public Resource<Coursier> toResource(Coursier coursier) {
		return new Resource<>(coursier
				);
	}
}
