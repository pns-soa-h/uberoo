package fr.unice.polytech.soa.uberoo.repository;

import fr.unice.polytech.soa.uberoo.model.Recu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecuRepository extends JpaRepository<Recu, Long> {

}
