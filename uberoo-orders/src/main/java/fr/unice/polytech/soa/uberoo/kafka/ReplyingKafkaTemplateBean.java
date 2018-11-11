package fr.unice.polytech.soa.uberoo.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReplyingKafkaTemplateBean {

	@Bean
	public ReplyingKafkaTemplate<String, String, String> kafkaTemplate(
			ProducerFactory<String, String> pf,
			KafkaMessageListenerContainer<String, String> replyContainer) {
		return new ReplyingKafkaTemplate<>(pf, replyContainer);
	}

	@Bean
	public KafkaMessageListenerContainer<String, String> replyContainer(
			ConsumerFactory<String, String> cf) {
		ContainerProperties containerProperties = new ContainerProperties("order_total_computed");
		return new KafkaMessageListenerContainer<>(cf, containerProperties);
	}
}
