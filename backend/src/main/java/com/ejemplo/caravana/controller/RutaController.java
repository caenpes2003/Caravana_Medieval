package com.ejemplo.caravana.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ejemplo.caravana.dto.RutaDTO;
import com.ejemplo.caravana.model.Ruta;
import com.ejemplo.caravana.service.PermisosService;
import com.ejemplo.caravana.service.RutaService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/rutas")
public class RutaController {

    private final RutaService rutaService;
    private final PermisosService permisosService;

    public RutaController(RutaService rutaService, PermisosService permisosService) {
        this.rutaService = rutaService;
        this.permisosService = permisosService;
    }

    /** POST: Crear una nueva ruta (solo administrador) */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RutaDTO> crearRuta(@RequestBody RutaDTO dto, HttpSession session) {
        permisosService.validarAdministrador(session);
        Ruta nueva = rutaService.crearRuta(dto);
        return ResponseEntity.ok(new RutaDTO(nueva));
    }

    /** PUT: Actualizar una ruta (solo administrador) */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RutaDTO> actualizarRuta(@PathVariable Long id, @RequestBody RutaDTO dto, HttpSession session) {
        permisosService.validarAdministrador(session);
        Ruta actualizada = rutaService.actualizarRuta(id, dto);
        return ResponseEntity.ok(new RutaDTO(actualizada));
    }

    /** DELETE: Eliminar una ruta (solo administrador) */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRuta(@PathVariable Long id, HttpSession session) {
        permisosService.validarAdministrador(session);
        rutaService.eliminarRuta(id);
        return ResponseEntity.noContent().build();
    }

    // --- (abiertos para todos) ---
    @GetMapping
    public List<RutaDTO> getFromCiudadActual() {
        return rutaService.findRutasDesdeCiudadActual().stream().map(RutaDTO::new).toList();
    }

    @GetMapping("/{id}")
    public RutaDTO getById(@PathVariable Long id) {
        return new RutaDTO(rutaService.findById(id));
    }
}
