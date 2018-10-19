package fr.unice.polytech.soa.uberoo;

import fr.unice.polytech.soa.uberoo.model.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class KafkaListenerBean {


	@KafkaListener(topics = "order")
	public void deliver(Order order, Acknowledgment acknowledgment) {
		System.out.println("Order completed : " + order.getId());
		acknowledgment.acknowledge();
	}

}
