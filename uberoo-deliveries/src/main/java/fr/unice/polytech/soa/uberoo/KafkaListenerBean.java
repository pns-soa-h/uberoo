package fr.unice.polytech.soa.uberoo;

import com.google.gson.Gson;
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

	@KafkaListener(topics = "order_status_accepted")
	public void deliver(String jsonOrder, Acknowledgment acknowledgment) {
		Gson gson = new Gson();
		Order order = gson.fromJson(jsonOrder, Order.class);
		orderRepository.save(order);
		acknowledgment.acknowledge();
	}

}
