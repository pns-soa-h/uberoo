package fr.unice.polytech.soa.uberoo.controller;

import fr.unice.polytech.soa.uberoo.assembler.CoursierResourceAssembler;
import fr.unice.polytech.soa.uberoo.assembler.LocationResourceAssembler;
import fr.unice.polytech.soa.uberoo.exception.CoursierNotFoundException;
import fr.unice.polytech.soa.uberoo.model.Coursier;
import fr.unice.polytech.soa.uberoo.model.Location;
import fr.unice.polytech.soa.uberoo.repository.CoursierRepository;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class GeolocationController {

	private final CoursierRepository repository;

	private final CoursierResourceAssembler assembler;

	private final LocationResourceAssembler locationAssembler;

	public GeolocationController(CoursierRepository repository, CoursierResourceAssembler assembler, LocationResourceAssembler locationAssembler) {
		this.repository = repository;
		this.assembler = assembler;
		this.locationAssembler = locationAssembler;
	}

	@GetMapping("/coursiers")
	public Resources<Resource<Coursier>> all(
			@RequestParam(value = "location", required = false) Location location) {
		List<Resource<Coursier>> coursiers = repository.findAll().stream()
				.map(assembler::toResource)
				.collect(Collectors.toList());
		return new Resources<>(coursiers,
				linkTo(methodOn(GeolocationController.class).all(location)).withSelfRel());
	}

	@GetMapping("/coursiers/{id}")
	public Resource<Coursier> one(@PathVariable Long id) {
		return assembler.toResource(
				repository.findById(id)
						.orElseThrow(() -> new CoursierNotFoundException(id)));
	}

	@GetMapping("/coursiers/{id}/location")
	public Resource<Location> location(@PathVariable Long id) {
		Optional <Coursier> coursier = repository.findById(id);
		if(coursier.isPresent()){
			return locationAssembler.toResource(coursier.get().getLocation());
		}
		throw new CoursierNotFoundException(id);
	}

}
