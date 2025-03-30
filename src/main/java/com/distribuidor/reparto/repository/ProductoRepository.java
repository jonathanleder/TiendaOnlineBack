package com.distribuidor.reparto.repository;

import com.distribuidor.reparto.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {


}
