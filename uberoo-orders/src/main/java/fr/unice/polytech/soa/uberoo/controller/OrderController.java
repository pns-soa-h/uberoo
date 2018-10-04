package fr.unice.polytech.soa.uberoo.controller;

import fr.unice.polytech.soa.uberoo.model.Order;
import fr.unice.polytech.soa.uberoo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @PostMapping(value = "/orders/new")
    public Order create(@RequestBody Order order) {
        return repository.save(order);
    }

    @GetMapping(value = "/orders/{id}")
    public Order show(@PathVariable("id") String id) {
        return repository.getOne(Long.parseLong(id));
    }

    /*
    @PutMapping("/orders/{id}")
    public Order update(@PathVariable("id") String id, @RequestBody Map<String, String> body) {
        return null;
    }
    */

    @PutMapping("orders/{id}")
    public Order assign(@PathVariable("id") String id, @RequestBody Map<String, String> body) {
        Long orderId = Long.parseLong(id);
        Order order = repository.getOne(orderId);
        order.setCoursierId(Long.parseLong(body.get("coursier_id")));
        return order;
    }
}
