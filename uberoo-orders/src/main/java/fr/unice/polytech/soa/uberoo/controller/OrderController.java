package fr.unice.polytech.soa.uberoo.controller;

import fr.unice.polytech.soa.uberoo.model.Order;
import fr.unice.polytech.soa.uberoo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class OrderController {

    private final OrderRepository repository;

    @Autowired
    public OrderController(OrderRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/orders")
    public List<Order> index() {
        // TODO: pagination to prevent stress
        return repository.findAll();
    }

    @PostMapping(value = "/orders/new"/*, consumes = "application/json"*/)
    public ResponseEntity<Order> create(@RequestBody Order order) {
        Order created = repository.save(order);
        // Check if created ?
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping(value = "/orders/{id}")
    public Resource<Order> show(@PathVariable("id") String id) {
        return new Resource<>(repository.getOne(Long.parseLong(id)));
    }

    /*
    @PutMapping("/orders/{id}")
    public Order update(@PathVariable("id") String id, @RequestBody Map<String, String> body) {
        return null;
    }
    */

    @RequestMapping(value = "/orders/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Order> assign(@PathVariable("id") String id, @RequestBody Map<String, String> body) {
        Order order = repository.getOne(Long.parseLong(id));
        order.setCoursierId(Long.parseLong(body.get("coursierId")));
        repository.save(order);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
