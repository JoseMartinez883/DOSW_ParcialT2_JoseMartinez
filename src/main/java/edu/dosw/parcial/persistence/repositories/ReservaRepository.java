package edu.dosw.parcial.persistence.repositories;

import edu.dosw.parcial.persistence.entities.ReservaEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends MongoRepository<ReservaEntity, String> {
    long countByFechaAndHoraAndEstado(String fecha, String hora, String estado);

    @org.springframework.data.mongodb.repository.Query("{ 'cliente.correo': ?0 }")
    List<ReservaEntity> findByClienteCorreo(String correo);

    @org.springframework.data.mongodb.repository.Query("{ 'cliente': { $exists: true } }")
    List<ReservaEntity> findAllReservas();
}
