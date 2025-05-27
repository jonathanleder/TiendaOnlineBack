package com.distribuidor.reparto.service.Product;

import com.distribuidor.reparto.modelo.Producto;
import com.distribuidor.reparto.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }

    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }
    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    public void eliminarProducto(Producto producto) {
        productoRepository.delete(producto);
    }
}
