package fr.unice.polytech.soa.uberoo.repository;

import fr.unice.polytech.soa.uberoo.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * JPA repository for storing meals.
 *
 * @author Julien Lemaire
 */
public interface MealRepository extends JpaRepository<Meal, Long> {

}
