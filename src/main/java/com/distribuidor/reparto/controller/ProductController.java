package com.distribuidor.reparto.controller;

import com.distribuidor.reparto.modelo.Producto;
import com.distribuidor.reparto.repository.ProductoRepository;
import com.distribuidor.reparto.service.Product.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/products/")
@CrossOrigin
public class ProductController {
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> obtenerTodos() {
        return productoService.obtenerProductos();
    }

    @GetMapping("/{id}")
    public Optional<Producto> obtenerProductoPorId(@RequestParam Long id) {
        return productoService.obtenerProductoPorId(id);
    }
    @PostMapping("/crear")
    public ResponseEntity<Producto> crearProducto(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") float precio,
            @RequestParam("stock") Integer stock,
            @RequestParam("imagen") MultipartFile imagen) {

        try {
            String imageUrl = uploadImage(imagen);

            Producto producto = new Producto();
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(precio);
            producto.setStock(stock);
            producto.setImagenUrl(imageUrl);

            return ResponseEntity.ok(productoRepository.save(producto));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String uploadImage(MultipartFile file)throws IOException {

        Path uploadPath = Paths.get(System.getProperty("user.dir")+"/uploads/");

        if(!Files.exists(uploadPath)) {
            Files.createDirectory(uploadPath);
        }
        String originalFileName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString();

        String extension = "";
        int dotIndex = originalFileName.lastIndexOf('.');
        if(dotIndex > 0) {
            extension = originalFileName.substring(dotIndex);
        }else{
            extension = ".jpg";
        }
        fileName += extension;
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "api/uploads/"+fileName;

    }

    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable Long id) {
        var producto = productoService.obtenerProductoPorId(id).orElseThrow(()-> new RuntimeException("Producto no encontrado"));
        productoService.eliminarProducto(producto);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestParam("nombre")String nombre , @RequestParam("descripcion")String descripcion, @RequestParam("precio")float precio, @RequestParam("stock")Integer stock, @RequestParam(value = "imagen", required = false)MultipartFile imagen ) {
        Optional<Producto> productoExistente = productoService.obtenerProductoPorId(id);
        if(productoExistente.isPresent()) {
            Producto producto = productoExistente.get();
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(precio);
            producto.setStock(stock);
            if(imagen != null && !imagen.isEmpty()) {
                try{
                    String imageUrl= uploadImage(imagen);
                    producto.setImagenUrl(imageUrl);
                }catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }
            Producto productoActualizado = productoService.guardarProducto(producto);
            return ResponseEntity.ok(productoActualizado);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
