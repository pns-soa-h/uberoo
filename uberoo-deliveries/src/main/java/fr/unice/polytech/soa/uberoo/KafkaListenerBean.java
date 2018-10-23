package fr.unice.polytech.soa.uberoo;

import fr.unice.polytech.soa.uberoo.model.Order;
import fr.unice.polytech.soa.uberoo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class KafkaListenerBean {

	@Autowired
	private OrderRepository orderRepository;

	@KafkaListener(topics = "order")
	public void deliver(Order order, Acknowledgment acknowledgment) {
		orderRepository.save(order);
		acknowledgment.acknowledge();
	}

}
