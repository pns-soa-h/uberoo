package fr.unice.polytech.soa.uberoo.repository;

import fr.unice.polytech.soa.uberoo.model.Coursier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursierRepository extends JpaRepository<Coursier, Long> {

}
