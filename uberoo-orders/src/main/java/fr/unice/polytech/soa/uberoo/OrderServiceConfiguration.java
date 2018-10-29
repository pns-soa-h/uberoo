package fr.unice.polytech.soa.uberoo;

import fr.unice.polytech.soa.uberoo.model.Order;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class OrderServiceConfiguration  extends RepositoryRestConfigurerAdapter {

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.exposeIdsFor(Order.class);
	}
}
