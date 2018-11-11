package fr.unice.polytech.soa.uberoo.controller;

import fr.unice.polytech.soa.uberoo.assembler.CouponResourceAssembler;
import fr.unice.polytech.soa.uberoo.exception.CouponNotFoundException;
import fr.unice.polytech.soa.uberoo.model.Coupon;
import fr.unice.polytech.soa.uberoo.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * @author Alexis Couvreur
 */
@RestController
public class CouponController {

	private final CouponRepository repository;

	private final CouponResourceAssembler assembler;

	@Autowired
	public CouponController(CouponRepository repository, CouponResourceAssembler assembler) {
		this.repository = repository;
		this.assembler = assembler;
	}

	@GetMapping("/coupons")
	public Resources<Resource<Coupon>> findAll(@RequestParam(value = "restaurant", required = false) Long restaurant) {

		List<Resource<Coupon>> coupons = repository.findAll().stream()
				.filter(c -> restaurant == null || c.getRestaurantId().equals(restaurant))
				.map(assembler::toResource)
				.collect(Collectors.toList());

		return new Resources<>(coupons,
				linkTo(methodOn(CouponController.class).findAll(null)).withSelfRel());
	}

	@GetMapping("/coupons/{id}")
	public Resource<Coupon> find(@PathVariable Long id) {
		return assembler.toResource(
				repository.findById(id)
						.orElseThrow(() -> new CouponNotFoundException(id)));
	}

	@PostMapping("/coupons")
	public ResponseEntity<Resource<Coupon>> create(@RequestBody Coupon coupon) {

		Coupon c = repository.save(coupon);

		return ResponseEntity
				.created(linkTo(methodOn(CouponController.class).find(c.getId())).toUri())
				.body(assembler.toResource(c));
	}
}
