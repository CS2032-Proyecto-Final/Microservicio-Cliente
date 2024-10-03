package org.ide.microserviciocliente.repository;

import org.ide.microserviciocliente.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    Optional<Cuenta> findByClienteId(Long clienteId);
}
