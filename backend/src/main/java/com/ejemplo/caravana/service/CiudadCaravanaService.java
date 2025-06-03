package com.ejemplo.caravana.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ejemplo.caravana.exception.ResourceNotFoundException;
import com.ejemplo.caravana.model.Caravana;
import com.ejemplo.caravana.model.Ciudad;
import com.ejemplo.caravana.model.CiudadCaravana;
import com.ejemplo.caravana.repository.CiudadCaravanaRepository;
import com.ejemplo.caravana.repository.CiudadCaravanaServicioRepository;
import com.ejemplo.caravana.repository.CiudadRepository;

@Service
public class CiudadCaravanaService {

    private final CiudadCaravanaRepository ciudadRepo;
    private final CiudadRepository ciudadBaseRepo;
    private final CiudadCaravanaServicioRepository servicioRepo;

    public CiudadCaravanaService(CiudadCaravanaRepository ciudadRepo, CiudadRepository ciudadBaseRepo, 
                                 CiudadCaravanaServicioRepository servicioRepo) {
        this.ciudadRepo = ciudadRepo;
        this.ciudadBaseRepo = ciudadBaseRepo;
        this.servicioRepo = servicioRepo;
    }

    /**
     * Retorna todas las ciudades de una caravana
     */
    public List<CiudadCaravana> findByCaravana(Caravana caravana) {
        return ciudadRepo.findByCaravana(caravana);
    }

    /**
     * Retorna una ciudad específica por ID si pertenece a la caravana dada
     */
    public CiudadCaravana findByIdAndCaravana(Long id, Caravana caravana) {
        return ciudadRepo.findByIdAndCaravana(id, caravana)
                .orElseThrow(() -> new ResourceNotFoundException("Ciudad no encontrada o no pertenece a tu caravana"));
    }

    /**
     * Crea una nueva ciudad asociada a la caravana
     */
    public CiudadCaravana crear(String nombre, double impuestos, Caravana caravana) {
        //1. Se crea la ciudad base (CIUDAD)

        Ciudad ciudadBase = ciudadBaseRepo
            .findByNombreIgnoreCase(nombre.trim())
            .orElseThrow(() -> new IllegalArgumentException(
                "La ciudad '" + nombre + "' aún no está en el catálogo"));

        if (ciudadRepo.existsByCaravanaAndCiudad(caravana, ciudadBase)) {
        throw new IllegalArgumentException(
            "Tu caravana ya contiene esa ciudad");
    }

        CiudadCaravana cc = new CiudadCaravana(
            ciudadBase.getNombre(),
            impuestos,
            caravana,
            ciudadBase);
    return ciudadRepo.save(cc);
    }

    /**
     * Actualiza una ciudad si pertenece a la caravana
     */
    public CiudadCaravana actualizar(Long id, String nombre, double impuestos, Caravana caravana) {
        CiudadCaravana ciudad = findByIdAndCaravana(id, caravana);
        ciudad.setNombre(nombre);
        ciudad.setImpuestoEntrada(impuestos);
        return ciudadRepo.save(ciudad);
    }

    /**
     * Elimina una ciudad si pertenece a la caravana
     */
    public void eliminar(Long id, Caravana caravana) {
        CiudadCaravana ciudad = findByIdAndCaravana(id, caravana);

        // 1. Eliminar servicios asociados
        servicioRepo.deleteByCiudadCaravana(ciudad);

        // 2. Eliminar la ciudad_caravana
        ciudadRepo.delete(ciudad);
    }
    public void guardar(CiudadCaravana cc) {
        ciudadRepo.save(cc);
    }
}
