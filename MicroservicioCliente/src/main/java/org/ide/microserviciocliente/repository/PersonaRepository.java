package org.ide.microserviciocliente.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ide.microserviciocliente.entity.Persona;

import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByTelefono(String telefono);
}
