package com.ejemplo.caravana.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ejemplo.caravana.service.CaravanaService;
import com.ejemplo.caravana.service.PermisosService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/caravana")
public class ImpuestoController {
    private static final Logger log = LoggerFactory.getLogger(ImpuestoController.class);
    private final CaravanaService svc;
    private final PermisosService permisosService;

    public ImpuestoController(CaravanaService svc, PermisosService permisosService) {
        this.svc = svc;
        this.permisosService = permisosService;
    }

    @PostMapping("/pagar-impuestos")
    public void pagarImpuestos(@RequestParam Long rutaId, HttpSession session) {
        permisosService.validarCaravanero(session);
        log.info("POST /pagar-impuestos?rutaId={}", rutaId);
        svc.pagarImpuestos(rutaId);
}

}
