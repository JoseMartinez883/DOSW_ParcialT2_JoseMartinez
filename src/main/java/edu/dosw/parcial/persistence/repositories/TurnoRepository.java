package edu.dosw.parcial.persistence.repositories;

import edu.dosw.parcial.persistence.entities.TurnoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TurnoRepository extends MongoRepository<TurnoEntity, String> {
    boolean existsByBarberoNombreIgnoreCaseAndFechaAndHora(String barberoNombre, String fecha, String hora);
    Optional<TurnoEntity> findByBarberoNombreIgnoreCaseAndFechaAndHora(String barberoNombre, String fecha, String hora);
    List<TurnoEntity> findByFechaAndHoraAndEstado(String fecha, String hora, String estado);
}
