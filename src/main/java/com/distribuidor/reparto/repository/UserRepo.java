package com.distribuidor.reparto.repository;

import com.distribuidor.reparto.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Usuario, Long> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);
}
