package fr.unice.polytech.soa.uberoo.repository;

import fr.unice.polytech.soa.uberoo.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Description
 *
 * @author Julien Lemaire
 */
@RepositoryRestResource(exported = false)
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
