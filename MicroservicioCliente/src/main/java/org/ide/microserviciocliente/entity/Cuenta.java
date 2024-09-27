package org.ide.microserviciocliente.entity;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Data
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double saldo;

    @OneToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
