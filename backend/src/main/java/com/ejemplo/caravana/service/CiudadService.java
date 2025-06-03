package com.ejemplo.caravana.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ejemplo.caravana.model.Ciudad;
import com.ejemplo.caravana.repository.CiudadRepository;

@Service
public class CiudadService {

    private final CiudadRepository ciudadRepo;

    public CiudadService(CiudadRepository ciudadRepo) {
        this.ciudadRepo = ciudadRepo;
    }

    public Ciudad crearCiudadBase(String nombre) {
        if (ciudadRepo.findByNombreIgnoreCase(nombre.trim()).isPresent()) {
            throw new IllegalArgumentException("La ciudad ya existe en el catálogo");
        }

        Ciudad ciudad = new Ciudad();
        ciudad.setNombre(nombre.trim());
        return ciudadRepo.save(ciudad);
    }

    public List<Ciudad> listarTodas() {
        return ciudadRepo.findAll();
    }
}
