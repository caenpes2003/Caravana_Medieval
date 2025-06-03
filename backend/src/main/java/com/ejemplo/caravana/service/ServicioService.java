package com.ejemplo.caravana.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ejemplo.caravana.exception.ResourceNotFoundException;
import com.ejemplo.caravana.model.Caravana;
import com.ejemplo.caravana.model.CiudadCaravana;
import com.ejemplo.caravana.model.CiudadCaravanaServicio;
import com.ejemplo.caravana.model.ServicioBase;
import com.ejemplo.caravana.model.ServicioEntity;
import com.ejemplo.caravana.repository.CaravanaRepository;
import com.ejemplo.caravana.repository.CiudadCaravanaServicioRepository;
import com.ejemplo.caravana.repository.ServicioBaseRepository;
import com.ejemplo.caravana.repository.ServicioRepository;

@Service
public class ServicioService {
    private final ServicioRepository repo;
    private final CiudadCaravanaServicioRepository ccsRepo;
    private final ServicioBaseRepository servicioBaseRepo;
    private final CaravanaRepository caravanaRepo;

    public ServicioService(ServicioRepository repo, CiudadCaravanaServicioRepository ccsRepo, 
                           ServicioBaseRepository servicioBaseRepo, CaravanaRepository caravanaRepo) {
        this.repo = repo;
        this.ccsRepo = ccsRepo;
        this.servicioBaseRepo = servicioBaseRepo;
        this.caravanaRepo = caravanaRepo;
    }


    /**
     * Lista todos los servicios disponibles.
    */

    public List<ServicioEntity> findByCaravanaId(Long caravanaId) {
            return repo.findByCaravanaId(caravanaId);
    }
    


    public ServicioEntity crearParaCaravana(Long caravanaId, String tipo, double costo) {
        
        Caravana caravana = caravanaRepo.findById(caravanaId)
        .orElseThrow(() -> new ResourceNotFoundException("Caravana no encontrada"));
        ServicioEntity s = new ServicioEntity();
        s.setTipo(tipo);
        s.setCosto(costo);
        s.setCaravana(caravana);
        return repo.save(s);
    }



    public ServicioEntity findById(Long id) {
        return repo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado: " + id));
    }
    public ServicioEntity crear(String tipo, double costo) {
    ServicioEntity nuevo = new ServicioEntity();
    nuevo.setTipo(tipo);
    nuevo.setCosto(costo);
    return repo.save(nuevo);
}

public ServicioEntity actualizar(Long id, String tipo, double costo) {
        ServicioEntity s = repo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado: " + id));
        s.setTipo(tipo);
        s.setCosto(costo);
        return repo.save(s);
    }



public void eliminar(Long id) {
        ccsRepo.deleteByServicio_Id(id);   
    repo.deleteById(id); 
    }


public void clonarServiciosBase(Caravana caravana) {
    List<ServicioEntity> serviciosBase = repo.findAll()
        .stream()
        .filter(s -> s.getCaravana() == null) // servicios base sin caravana asociada
        .toList();

    List<ServicioEntity> clonados = serviciosBase.stream()
        .map(s -> {
            ServicioEntity nuevo = new ServicioEntity();
            nuevo.setTipo(s.getTipo());
            nuevo.setCosto(s.getCosto());
            nuevo.setCaravana(caravana);
            return nuevo;
        })
        .toList();

    repo.saveAll(clonados);
    }

    /* ───────────────── Clonado base ───────────────── */

    /** Clona todas las plantillas de servicio_base para la caravana */
    public void clonarServiciosBaseParaCaravana(Caravana caravana) {

        List<ServicioBase> plantillas = servicioBaseRepo.findAll();   // ✅ genérico
        List<ServicioEntity> clonados = plantillas.stream()
            .map(p -> {
                ServicioEntity e = new ServicioEntity();
                e.setTipo(p.getTipo());
                e.setCosto(p.getCosto());
                e.setCaravana(caravana);
                return e;
            })
            .toList();

        repo.saveAll(clonados);
    }




/** Crea filas en ciudad_caravana_servicio */
    public void vincularServiciosACiudades(Caravana caravana,
                                           List<CiudadCaravana> ciudades) {

        List<ServicioEntity> servicios = repo.findByCaravanaId(caravana.getId());

        List<CiudadCaravanaServicio> relaciones = ciudades.stream()
            .flatMap(ciud -> servicios.stream()
                    .map(srv -> new CiudadCaravanaServicio(ciud, srv)))
            .toList();

        ccsRepo.saveAll(relaciones);
    }
}