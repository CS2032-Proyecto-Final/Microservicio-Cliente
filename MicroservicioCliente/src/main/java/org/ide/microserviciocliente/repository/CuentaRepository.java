package org.ide.microserviciocliente.repository;

import org.ide.microserviciocliente.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
}
