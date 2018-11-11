package fr.unice.polytech.soa.uberoo.controller;

import fr.unice.polytech.soa.uberoo.assembler.CoursierResourceAssembler;
import fr.unice.polytech.soa.uberoo.assembler.LocationResourceAssembler;
import fr.unice.polytech.soa.uberoo.exception.BodyMemberNotFoundException;
import fr.unice.polytech.soa.uberoo.exception.CoursierNotFoundException;
import fr.unice.polytech.soa.uberoo.exception.MalformedException;
import fr.unice.polytech.soa.uberoo.model.Coursier;
import fr.unice.polytech.soa.uberoo.model.Location;
import fr.unice.polytech.soa.uberoo.repository.CoursierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class GeolocationController {

	private final CoursierRepository repository;

	private final CoursierResourceAssembler assembler;

	private final LocationResourceAssembler locationAssembler;

	@Autowired
	public GeolocationController(CoursierRepository repository, CoursierResourceAssembler assembler, LocationResourceAssembler locationAssembler) {
		this.repository = repository;
		this.assembler = assembler;
		this.locationAssembler = locationAssembler;
	}

	@GetMapping("/coursiers")
	public Resources <Resource <Coursier>> all() {
		List <Resource <Coursier>> coursiers = repository.findAll().stream()
				.map(assembler::toResource)
				.collect(Collectors.toList());
		return new Resources <>(coursiers,
				linkTo(methodOn(GeolocationController.class).all()).withRel("coursiers"));
	}


	@PostMapping(value = "/coursiers", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Resource<Coursier>> newCoursier(@RequestBody Coursier coursier) {

		System.out.println(coursier);

		return ResponseEntity
				.created(linkTo(methodOn(GeolocationController.class).one(coursier.getId())).toUri())
				.body(assembler.toResource(coursier));
	}

	@GetMapping("/coursiers/{id}")
	public Resource <Coursier> one(@PathVariable Long id) {
		return assembler.toResource(
				repository.findById(id)
						.orElseThrow(() -> new CoursierNotFoundException(id)));
	}

	@PatchMapping(value = "/coursiers/{id}/location")
	public ResponseEntity<ResourceSupport> updateLocation(@PathVariable Long id, @RequestBody Map<String, String> map) {
		String strLocation = map.get("location");
		if(strLocation == null) {
			throw new BodyMemberNotFoundException("location");
		}

		Coursier coursier = repository.findById(id).orElseThrow(() -> new CoursierNotFoundException(id));
		Location location = new Location(strLocation);

			// Retrieve coursierId from body and check if present
			String coursierId = map.get("coursierId");
			if (coursierId == null) {
				throw new BodyMemberNotFoundException("coursierId");
			}

			// Check if header is well formed
			try {
				coursier.setId(Long.parseLong(coursierId));
			} catch (NumberFormatException ex) {
				throw new MalformedException("coursierId", "number");
			}

		coursier.setLocation(location);

		return ResponseEntity.ok(assembler.toResource(repository.save(coursier)));

	}

	@GetMapping("/coursiers/{id}/location")
	public Resource <Location> location(@PathVariable Long id) {
		return locationAssembler.toResource(
				repository.findById(id)
						.orElseThrow(() -> new CoursierNotFoundException(id))
						.getLocation());
	}

}
