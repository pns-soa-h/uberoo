package fr.unice.polytech.soa.uberoo.controller.orders;

import fr.unice.polytech.soa.uberoo.assembler.OrderResourceAssembler;
import fr.unice.polytech.soa.uberoo.exception.BodyMemberNotFoundException;
import fr.unice.polytech.soa.uberoo.model.orders.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.*;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.client.Hop.rel;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class OrderController {

	private final OrderResourceAssembler assembler;
	private final Traverson traverson;
	private final RestTemplate restTemplate;

	@Autowired
    public OrderController(OrderResourceAssembler assembler) throws URISyntaxException {
        this.assembler = assembler;
        this.traverson = new Traverson(new URI("http://uberoo-orders:8080"), MediaTypes.HAL_JSON);
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        this.restTemplate = new RestTemplate(requestFactory);
    }

	@GetMapping("/orders")
	public Resources<Resource<Order>> all(
			@RequestParam(value = "status", required = false)Order.Status status,
			@RequestParam(value = "restaurant", required = false) Long restaurantId) {

		Traverson.TraversalBuilder tb = traverson.follow(rel("orders"))
				.follow(rel("self").withParameter("status", status).withParameter("restaurant", restaurantId));
		ParameterizedTypeReference<Resources<Resource<Order>>> typeReference = new ParameterizedTypeReference<Resources<Resource<Order>>>() {};
		Resources<Resource<Order>> data = tb.toObject(typeReference);

		List<Resource<Order>> req = new LinkedList<>();
		for (Resource<Order> r : data) {
			req.add(assembler.toResource(r.getContent()));
		}

		return new Resources<>(req,
				linkTo(methodOn(OrderController.class).all(null, null)).withSelfRel(),
				linkTo(methodOn(OrderController.class).one(null)).withRel("item"));
	}

	@GetMapping("/orders/{id}")
	public Resource<Order> one(@PathVariable Long id) {
		Traverson.TraversalBuilder tb = traverson.follow(rel("orders"))
				.follow(rel("item").withParameter("id", id));
		ParameterizedTypeReference<Resource<Order>> typeReference = new ParameterizedTypeReference<Resource<Order>>() {};
		return assembler.toResource(tb.toObject(typeReference).getContent());
	}

	@PostMapping(value = "/orders", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Resource<Order>> newOrder(@RequestBody Order order) {
		Traverson.TraversalBuilder tb = traverson.follow(rel("orders"))
				.follow(rel("self"));
		ParameterizedTypeReference<Resource<Order>> typeReference = new ParameterizedTypeReference<Resource<Order>>() {};

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<Order> httpEntity = new HttpEntity<>(order, httpHeaders);
		return restTemplate.exchange(tb.asLink().getHref(), HttpMethod.POST, httpEntity, typeReference);
	}


	@PatchMapping("/orders/{id}/status")
	public ResponseEntity<ResourceSupport> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> map) {

		Traverson.TraversalBuilder tb = traverson.follow(rel("orders"))
				.follow(rel("item").withParameter("id", id))
				.follow(rel("update"));
		ParameterizedTypeReference<ResourceSupport> typeReference = new ParameterizedTypeReference<ResourceSupport>() {};

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

		HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(map, httpHeaders);

		return restTemplate.exchange(tb.asLink().getHref(), HttpMethod.PATCH, httpEntity, typeReference);
	}
}
