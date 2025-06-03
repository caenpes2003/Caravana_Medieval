package com.ejemplo.caravana.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ejemplo.caravana.dto.HistorialDTO;
import com.ejemplo.caravana.service.HistorialService;

@RestController
@RequestMapping("/api/caravana/historial")
public class HistorialController {
    private static final Logger log = LoggerFactory.getLogger(HistorialController.class);
    private final HistorialService historialService;

    public HistorialController(HistorialService historialService) {
        this.historialService = historialService;
    }

    @GetMapping
    public List<HistorialDTO> listar(
        @RequestParam(name = "caravanaId", defaultValue = "1") Long caravanaId
    ) {
        log.info("GET /api/caravana/historial?caravanaId={}", caravanaId);
        return historialService.getHistorial(caravanaId);
    }
}
