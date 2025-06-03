package com.ejemplo.caravana.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ejemplo.caravana.dto.HistorialDTO;
import com.ejemplo.caravana.model.Historial;
import com.ejemplo.caravana.repository.HistorialRepository;

@Service
@Transactional
public class HistorialService {
    private static final Logger log = LoggerFactory.getLogger(HistorialService.class);
    private final HistorialRepository historialRepo;

    public HistorialService(HistorialRepository historialRepo) {
        this.historialRepo = historialRepo;
    }

    public List<HistorialDTO> getHistorial(Long caravanaId) {
        log.info("Obteniendo historial para caravana id={}", caravanaId);
        return historialRepo.findByCaravanaIdOrderByTimestampDesc(caravanaId)
            .stream()
            .map(h -> new HistorialDTO(
                h.getId(),
                h.getTimestamp(),
                h.getTipo(),
                h.getDetalle()
            ))
            .collect(Collectors.toList());
    }

    public void registrar(Historial h) {
        log.info("Registrando evento {}: {}", h.getTipo(), h.getDetalle());
        historialRepo.save(h);
    }
    /** Borra todo el historial de la caravana dada */
    public void borrarPorCaravana(Long caravanaId) {
        historialRepo.deleteByCaravanaId(caravanaId);
    }
}