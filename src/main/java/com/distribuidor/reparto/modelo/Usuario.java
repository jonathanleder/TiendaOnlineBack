package com.distribuidor.reparto.modelo;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@AllArgsConstructor
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, updatable=false, name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column
    private String telefono;


    @Column(name = "enabled",nullable = false)
    private boolean enabled = false;


    public Usuario(String nombre,String apellido,String telefono,String username,String password,String email){
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fechaRegistro = LocalDateTime.now();
    }
    public Usuario() {
        // Este constructor sin parámetros se usa para la creación de entidades en JPA.
    }


    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return this.email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
