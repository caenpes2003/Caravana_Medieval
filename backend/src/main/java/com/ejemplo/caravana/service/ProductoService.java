package com.ejemplo.caravana.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ejemplo.caravana.exception.ResourceNotFoundException;
import com.ejemplo.caravana.model.Producto;
import com.ejemplo.caravana.repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository repo;

    public ProductoService(ProductoRepository repo) {
        this.repo = repo;
    }

    public List<Producto> findAll() {
        return repo.findAll();
    }

    public Producto findById(Long id) {
        return repo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + id));
    }

    /** Crear un nuevo producto */
    public Producto crear(String nombre) {
        Producto p = new Producto();
        p.setNombre(nombre);
        return repo.save(p);
    }

    /** Actualizar un producto existente */
    public Producto actualizar(Long id, String nombre) {
        Producto existente = findById(id);
        existente.setNombre(nombre);
        return repo.save(existente);
    }

    /** Eliminar un producto */
    public void eliminar(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado: " + id);
        }
        repo.deleteById(id);
    }
}
