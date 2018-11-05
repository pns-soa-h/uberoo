package fr.unice.polytech.soa.uberoo.repository;

import fr.unice.polytech.soa.uberoo.model.Recipe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}
