package org.ide.microserviciocliente.repository;

import org.ide.microserviciocliente.entity.Tienda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TiendaRepository extends JpaRepository<Tienda, Long> {
    Optional<Tienda> findByCorreo(String correo);


}
