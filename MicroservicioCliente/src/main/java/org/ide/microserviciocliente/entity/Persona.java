package org.ide.microserviciocliente.entity;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Data
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String telefono;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
