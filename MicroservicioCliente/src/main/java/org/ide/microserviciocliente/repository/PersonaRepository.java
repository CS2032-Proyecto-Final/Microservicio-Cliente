package org.ide.microserviciocliente.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ide.microserviciocliente.entity.Persona;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByTelefono(String telefono);
    Optional<Persona> findByClienteId(Long clienteId);

    @Query("SELECT p.cliente.id, p.cliente.nombre FROM Persona p WHERE p.cliente.id IN :clienteIds")
    List<Object[]> findClienteIdAndNombreByClienteIdIn(@Param("clienteIds") List<Long> clienteIds);
}
