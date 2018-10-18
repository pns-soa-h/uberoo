package fr.unice.polytech.soa.uberoo;

import fr.unice.polytech.soa.uberoo.model.Order;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class KafkaListener {


	@org.springframework.kafka.annotation.KafkaListener(topics = "order")
	public void deliver(Order order, Acknowledgment acknowledgment) {
		System.out.println("Order completed : " + order.getId());
		acknowledgment.acknowledge();
	}

}
