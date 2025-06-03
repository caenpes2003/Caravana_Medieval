package com.ejemplo.caravana.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ejemplo.caravana.dto.RutaDTO;
import com.ejemplo.caravana.exception.ResourceNotFoundException;
import com.ejemplo.caravana.model.Caravana;
import com.ejemplo.caravana.model.CiudadCaravana;
import com.ejemplo.caravana.model.Ruta;
import com.ejemplo.caravana.repository.CaravanaRepository;
import com.ejemplo.caravana.repository.CiudadCaravanaRepository;
import com.ejemplo.caravana.repository.RutaRepository;

@Service
@Transactional
public class RutaService {

    private final RutaRepository rutaRepo;
    private final CaravanaRepository caravanaRepo;
    private final CiudadCaravanaRepository ciudadCaravanaRepo;

    public RutaService(
            RutaRepository rutaRepo,
            CaravanaRepository caravanaRepo,
            CiudadCaravanaRepository ciudadCaravanaRepo) {
        this.rutaRepo = rutaRepo;
        this.caravanaRepo = caravanaRepo;
        this.ciudadCaravanaRepo = ciudadCaravanaRepo;
    }

    /** Busca una ruta por ID o lanza 404 */
    public Ruta findById(Long id) {
        return rutaRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada: " + id));
    }

    /** Rutas que parten desde la ciudad actual de la caravana */
    public List<Ruta> findRutasDesdeCiudadActual() {
        Caravana c = caravanaRepo.findById(1L)
            .orElseThrow(() -> new ResourceNotFoundException("Caravana no encontrada"));

        if (c.getCiudadActualId() == null) {
            throw new ResourceNotFoundException("La ciudad actual de la caravana no está definida");
        }

        return rutaRepo.findByCiudadOrigen_Id(c.getCiudadActualId());
    }

    /** Crear nueva ruta entre ciudades de la misma caravana */
    public Ruta crearRuta(RutaDTO dto) {

        CiudadCaravana origen = ciudadCaravanaRepo.findById(dto.getCiudadOrigenId())
            .orElseThrow(() -> new ResourceNotFoundException("Ciudad origen no encontrada"));

        CiudadCaravana destino = ciudadCaravanaRepo.findById(dto.getCiudadDestinoId())
            .orElseThrow(() -> new ResourceNotFoundException("Ciudad destino no encontrada"));

        Ruta ruta = new Ruta();
        ruta.setCiudadOrigen(origen);
        ruta.setCiudadDestino(destino);
        ruta.setDistancia(dto.getDistancia());
        ruta.setSegura(dto.isSegura());
        ruta.setDano(dto.getDano());
        ruta.setTipoDanio(dto.getTipoDanio());
        ruta.setImpuestoDestino(dto.getImpuestoDestino());
        ruta.setVelocidad(dto.getVelocidad());
        ruta.setTiempo(dto.getTiempo());

        return rutaRepo.save(ruta);
    }

    /** Actualizar ruta existente */
    public Ruta actualizarRuta(Long id, RutaDTO dto) {
        Ruta ruta = findById(id);

        CiudadCaravana origen = ciudadCaravanaRepo.findById(dto.getCiudadOrigenId())
            .orElseThrow(() -> new ResourceNotFoundException("Ciudad origen no encontrada"));

        CiudadCaravana destino = ciudadCaravanaRepo.findById(dto.getCiudadDestinoId())
            .orElseThrow(() -> new ResourceNotFoundException("Ciudad destino no encontrada"));

        ruta.setCiudadOrigen(origen);
        ruta.setCiudadDestino(destino);
        ruta.setDistancia(dto.getDistancia());
        ruta.setSegura(dto.isSegura());
        ruta.setDano(dto.getDano());
        ruta.setTipoDanio(dto.getTipoDanio());
        ruta.setImpuestoDestino(dto.getImpuestoDestino());
        ruta.setVelocidad(dto.getVelocidad());
        ruta.setTiempo(dto.getTiempo());

        return rutaRepo.save(ruta);
    }

    /** Eliminar ruta */
    public void eliminarRuta(Long id) {
        if (!rutaRepo.existsById(id)) {
            throw new ResourceNotFoundException("Ruta no encontrada: " + id);
        }
        rutaRepo.deleteById(id);
    }
}
