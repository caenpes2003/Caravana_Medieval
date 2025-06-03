
package com.ejemplo.caravana.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ejemplo.caravana.dto.ServicioDTO;
import com.ejemplo.caravana.exception.ResourceNotFoundException;
import com.ejemplo.caravana.model.CiudadCaravanaServicio;
import com.ejemplo.caravana.repository.CiudadCaravanaServicioRepository;

@Service
public class ServicioCiudadService {

    private final CiudadCaravanaServicioRepository repo;

    public ServicioCiudadService(CiudadCaravanaServicioRepository repo) {
        this.repo = repo;
    }

    public List<ServicioDTO> obtenerServiciosDeCiudad(Long ciudadId) {

        List<CiudadCaravanaServicio> entidades =
            repo.findByCiudadCaravanaId(ciudadId);

        if (entidades.isEmpty())
            throw new ResourceNotFoundException(
                "La ciudad " + ciudadId + " no tiene servicios configurados");

        return entidades.stream()
                        .map(ccs -> new ServicioDTO(
                                ccs.getServicio().getId(),
                                ccs.getServicio().getTipo(),
                                ccs.getServicio().getCosto()))
                        .collect(Collectors.toList());
    }
}
