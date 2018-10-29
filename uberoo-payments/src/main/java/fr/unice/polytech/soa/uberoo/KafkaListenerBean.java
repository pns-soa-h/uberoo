package fr.unice.polytech.soa.uberoo;

import fr.unice.polytech.soa.uberoo.model.Addition;
import fr.unice.polytech.soa.uberoo.model.Order;
import fr.unice.polytech.soa.uberoo.model.Recipe;
import fr.unice.polytech.soa.uberoo.repository.OrderRepository;
import fr.unice.polytech.soa.uberoo.repository.RecipeRepository;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class KafkaListenerBean {



	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private RecipeRepository recipeRepository;
	
	@Autowired
	private KafkaTemplate<String, Recipe> recipeTemplate;
	
	@Autowired
	private KafkaTemplate<String, String> errorTemplate;
	
	
	
	@KafkaListener(topics = "payment")
	public void deliver(Addition payment, Acknowledgment acknowledgment) {
		Order o = orderRepository.findById(payment.getOrderId()).orElse(null);
		if(o == null) {
			errorTemplate.send("paymentError", "Order : "+payment.getOrderId()+" does not exist");
			return;
		}
		if(recipeRepository.findById(payment.getOrderId()).orElse(null)!=null) {
			errorTemplate.send("paymentError", "Order : "+payment.getOrderId()+" is already paid");
			return;
		}
		recipeTemplate.send("recipe", new Recipe(payment.getOrderId(),payment.getMontant(),new Date(System.currentTimeMillis())));
		acknowledgment.acknowledge();
	}

}