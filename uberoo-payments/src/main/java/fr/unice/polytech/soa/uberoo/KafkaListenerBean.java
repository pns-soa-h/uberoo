package fr.unice.polytech.soa.uberoo;

import fr.unice.polytech.soa.uberoo.model.Addition;
import fr.unice.polytech.soa.uberoo.model.Order;
import fr.unice.polytech.soa.uberoo.model.Recu;
import fr.unice.polytech.soa.uberoo.repository.OrderRepository;
import fr.unice.polytech.soa.uberoo.repository.RecuRepository;

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
	private RecuRepository recuRepository;
	
	private KafkaTemplate<String, Recu> kafkaTemplate;
	
	
	@KafkaListener(topics = "payment")
	public void deliver(Addition payment, Acknowledgment acknowledgment) {
		acknowledgment.acknowledge();
		Order o = orderRepository.findById(payment.getOrderId()).orElse(null);
		if(o == null) {
			//do order not found
			return;
		}
		if(recuRepository.findById(payment.getOrderId()).orElse(null)!=null) {
			//do alreadt paid;
			return;
		}
		kafkaTemplate.send("recu", new Recu(payment.getOrderId(),payment.getMontant(),new Date(System.currentTimeMillis())));
		
	}

}
