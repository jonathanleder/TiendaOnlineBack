package com.distribuidor.reparto.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "productos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;
    @Lob
    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Float precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = true)
    private String imagenUrl;


}
