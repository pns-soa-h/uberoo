package fr.unice.polytech.soa.uberoo.kafka;

import fr.unice.polytech.soa.uberoo.exception.OrderNotFoundException;
import fr.unice.polytech.soa.uberoo.model.Order;
import fr.unice.polytech.soa.uberoo.model.Recipe;
import fr.unice.polytech.soa.uberoo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

	private final OrderRepository repository;

	// ReplyingKafkaTemplate
	@Autowired
	public KafkaConsumer(OrderRepository repository) {
		this.repository = repository;
	}

	/*@KafkaListener(topics = "order_total_computed")
	public void onTotalComputed(Order order, Acknowledgment acknowledgment) {
		Order o = repository.findById(order.getId()).orElseThrow(() -> new OrderNotFoundException(order.getId()));
		acknowledgment.acknowledge();
	}*/

	@KafkaListener(topics = "recipe")
	public void onOrderReceipe(Recipe recipe, Acknowledgment acknowledgment) {
		Order o = repository.findById(recipe.getOrderId()).orElseThrow(() -> new OrderNotFoundException(recipe.getOrderId()));
		o.getPaymentDetails().setPaid(true);
		o.getPaymentDetails().setMethodId("cb");
		o.getPaymentDetails().setMethodTitle("Carte bancaire");
		repository.save(o);
		acknowledgment.acknowledge();
	}
}
