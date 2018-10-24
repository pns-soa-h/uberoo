package fr.unice.polytech.soa.uberoo.controller;

import fr.unice.polytech.soa.uberoo.assembler.CoursierResourceAssembler;
import fr.unice.polytech.soa.uberoo.model.Coursier;
import fr.unice.polytech.soa.uberoo.repository.CoursierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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
	public Resources<Resource<Coursier>> all() {
		List<Resource<Coursier>> coursiers = repository.findAll().stream()
				.map(assembler::toResource)
				.collect(Collectors.toList());

		return new Resources<>(coursiers,
			linkTo(methodOn(CoursierController.class).all()).withSelfRel());
	}
}
