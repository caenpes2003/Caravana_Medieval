package com.ejemplo.caravana.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ejemplo.caravana.model.Ciudad;
import com.ejemplo.caravana.service.CiudadService;
import com.ejemplo.caravana.service.PermisosService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/ciudades/catalogo")
public class CiudadBaseController {

    private final CiudadService ciudadService;
    private final PermisosService permisosService;

    

    public CiudadBaseController(CiudadService ciudadService, PermisosService permisosService) {
        this.ciudadService = ciudadService;
        this.permisosService = permisosService;
    }

    @PostMapping
    public ResponseEntity<Ciudad> crear(@RequestBody Map<String, String> body, HttpSession session) {
        permisosService.validarAdministrador(session);
        String nombre = body.get("nombre");
        Ciudad ciudad = ciudadService.crearCiudadBase(nombre);
        return ResponseEntity.ok(ciudad);
    }

    @GetMapping
    public List<Ciudad> listar() {
        return ciudadService.listarTodas();
    }

    

}
