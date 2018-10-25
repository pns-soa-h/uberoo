package fr.unice.polytech.soa.uberoo.controller;

import fr.unice.polytech.soa.uberoo.assembler.CoursierResourceAssembler;
import fr.unice.polytech.soa.uberoo.exception.CoursierNotFoundException;
import fr.unice.polytech.soa.uberoo.model.Coursier;
import fr.unice.polytech.soa.uberoo.repository.CoursierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class CoursierController {

	private final CoursierRepository repository;
	private final CoursierResourceAssembler assembler;

	@Autowired
	public CoursierController(CoursierRepository repository, CoursierResourceAssembler assembler) {
		this.repository = repository;
		this.assembler = assembler;
	}

	@GetMapping("/coursiers")
	public Resources<Resource<Coursier>> all(@RequestParam(value = "name", required = false)String name) {
		Stream<Coursier> coursiersStream = repository.findAll().stream();

		if (name != null) {
			coursiersStream = coursiersStream.filter(c -> c.getName().toLowerCase().equals(name.toLowerCase()));
		}

		List<Resource<Coursier>> coursiers = coursiersStream
				.map(assembler::toResource)
				.collect(Collectors.toList());

		return new Resources<>(coursiers,
			linkTo(methodOn(CoursierController.class).all(null)).withSelfRel());
	}

	@GetMapping("/coursiers/{id}")
	public Resource<Coursier> one(@PathVariable Long id) {
		Coursier coursier = repository.findById(id).orElseThrow(() -> new CoursierNotFoundException(id));

		return assembler.toResource(coursier);
	}
}
