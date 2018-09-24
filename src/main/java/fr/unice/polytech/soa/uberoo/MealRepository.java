package fr.unice.polytech.soa.uberoo;

import fr.unice.polytech.soa.uberoo.model.Meal;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by Alexis Couvreur on 9/25/2018.
 */
@RepositoryRestResource(collectionResourceRel = "meal", path = "meal")
public interface MealRepository extends PagingAndSortingRepository<Meal, Long> {

    List<Meal> browseMeals();

}
