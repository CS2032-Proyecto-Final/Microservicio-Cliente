package org.ide.microserviciocliente.entity;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Data
public class Tienda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String correo;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
