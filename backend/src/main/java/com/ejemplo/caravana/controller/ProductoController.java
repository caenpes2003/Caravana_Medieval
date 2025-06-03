package com.ejemplo.caravana.controller;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ejemplo.caravana.dto.ProductoDTO;
import com.ejemplo.caravana.model.Producto;
import com.ejemplo.caravana.service.ProductoService;
import com.ejemplo.caravana.service.PermisosService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final PermisosService permisosService;

    public ProductoController(ProductoService productoService, PermisosService permisosService) {
        this.productoService = productoService;
        this.permisosService = permisosService;
    }

    /** GET: listar todos los productos */
    @GetMapping
    public List<ProductoDTO> getAll() {
        return productoService.findAll().stream()
            .map(p -> new ProductoDTO(p.getId(), p.getNombre()))
            .collect(Collectors.toList());
    }

    /** GET: detalle por ID */
    @GetMapping("/{id}")
    public ProductoDTO getById(@PathVariable Long id) {
        Producto p = productoService.findById(id);
        return new ProductoDTO(p.getId(), p.getNombre());
    }

    /** POST: crear nuevo producto (solo ADMINISTRADOR) */
    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@RequestBody ProductoDTO dto, HttpSession session) {
        permisosService.validarAdministrador(session);
        Producto nuevo = productoService.crear(dto.getNombre());
        return ResponseEntity.ok(new ProductoDTO(nuevo.getId(), nuevo.getNombre()));
    }

    /** PUT: actualizar producto existente (solo ADMINISTRADOR) */
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(@PathVariable Long id, @RequestBody ProductoDTO dto, HttpSession session) {
        permisosService.validarAdministrador(session);
        Producto actualizado = productoService.actualizar(id, dto.getNombre());
        return ResponseEntity.ok(new ProductoDTO(actualizado.getId(), actualizado.getNombre()));
    }

    /** DELETE: eliminar producto (solo ADMINISTRADOR) */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, HttpSession session) {
        permisosService.validarAdministrador(session);
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
