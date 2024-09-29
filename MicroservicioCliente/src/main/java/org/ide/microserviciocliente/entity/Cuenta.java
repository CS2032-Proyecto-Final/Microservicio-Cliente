package org.ide.microserviciocliente.entity;

import lombok.Data;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Data
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double saldo;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
