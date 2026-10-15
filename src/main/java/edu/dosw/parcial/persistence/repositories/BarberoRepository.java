package edu.dosw.parcial.persistence.repositories;

import edu.dosw.parcial.persistence.entities.BarberoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BarberoRepository extends MongoRepository<BarberoEntity, String> {
    Optional<BarberoEntity> findByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
}
