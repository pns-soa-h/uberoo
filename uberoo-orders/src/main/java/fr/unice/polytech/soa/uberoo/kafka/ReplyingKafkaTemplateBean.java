package fr.unice.polytech.soa.uberoo.kafka;

import fr.unice.polytech.soa.uberoo.model.Bill;
import fr.unice.polytech.soa.uberoo.model.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReplyingKafkaTemplateBean {

	@Bean
	public ReplyingKafkaTemplate<String, Order, Bill> kafkaTemplate(
			ProducerFactory<String, Order> pf,
			KafkaMessageListenerContainer<String, Bill> replyContainer) {
		return new ReplyingKafkaTemplate<>(pf, replyContainer);
	}

	@Bean
	public KafkaMessageListenerContainer<String, Bill> replyContainer(
			ConsumerFactory<String, Bill> cf) {
		ContainerProperties containerProperties = new ContainerProperties("order_total_computed");
		return new KafkaMessageListenerContainer<>(cf, containerProperties);
	}
}
