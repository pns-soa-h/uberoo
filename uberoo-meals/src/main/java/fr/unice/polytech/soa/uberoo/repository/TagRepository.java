package fr.unice.polytech.soa.uberoo.repository;

import fr.unice.polytech.soa.uberoo.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * JPA repository for tags
 *
 * @author Julien Lemaire
 */
@RepositoryRestResource(exported = false)
public interface TagRepository extends JpaRepository<Tag, Long> {

}
