package fr.unice.polytech.soa.uberoo;

import fr.unice.polytech.soa.uberoo.model.Meal;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

/**
 * Configuration class for Meal service.
 *
 * @author Julien Lemaire
 */
@Configuration
public class MealServiceConfiguration extends RepositoryRestConfigurerAdapter {

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.exposeIdsFor(Meal.class);
	}
}
