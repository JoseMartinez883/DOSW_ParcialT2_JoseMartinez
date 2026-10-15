package edu.dosw.parcial.persistence.repositories;

import edu.dosw.parcial.persistence.entities.ServicioEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicioRepository extends MongoRepository<ServicioEntity, String> {
    Optional<ServicioEntity> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
    List<ServicioEntity> findByCodigoIn(List<String> codigos);
}
