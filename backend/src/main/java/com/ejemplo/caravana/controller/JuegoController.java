package com.ejemplo.caravana.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ejemplo.caravana.dto.JuegoIniciarRequest;
import com.ejemplo.caravana.dto.JuegoIniciarResponse;


import com.ejemplo.caravana.service.JuegoService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/juego")
public class JuegoController {

    private static final Logger log = LoggerFactory.getLogger(JuegoController.class);
    private final JuegoService juegoService;
   

    @Autowired
    public JuegoController(JuegoService juegoService) {
        this.juegoService = juegoService;
        
    }

    /** POST /api/juego/iniciar → seleccionar ciudades y crear rutas dinámicas */
    @PostMapping("/iniciar")
    @ResponseStatus(HttpStatus.CREATED)
    public JuegoIniciarResponse iniciarPartida(HttpSession session, @RequestBody JuegoIniciarRequest req){
    System.out.println("🔵 [Controller] Iniciando partida...");
    System.out.println("🔵 Datos recibidos: " + req);

    log.info("POST /api/juego/iniciar → ciudades={}", req.getCiudadIds());

    return juegoService.iniciarPartida(session, req);  
    }
}
