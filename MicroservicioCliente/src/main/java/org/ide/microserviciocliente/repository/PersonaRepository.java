package org.ide.microserviciocliente.repository;

import org.ide.microserviciocliente.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByTelefono(String telefono);
}
