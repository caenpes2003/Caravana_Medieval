package com.ejemplo.caravana.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ejemplo.caravana.dto.AuthResponse;
import com.ejemplo.caravana.dto.JugadorDTO;
import com.ejemplo.caravana.dto.LoginRequest;
import com.ejemplo.caravana.dto.RegistroRequest;
import com.ejemplo.caravana.model.Caravana;
import com.ejemplo.caravana.model.Jugador;
import com.ejemplo.caravana.model.Rol;
import com.ejemplo.caravana.repository.JugadorRepository;
import com.ejemplo.caravana.service.CaravanaService;
import com.ejemplo.caravana.service.JugadorService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JugadorRepository jugadorRepository;
    @Autowired
    private JugadorService jugadorService;
    @Autowired
    private CaravanaService caravanaService;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);



    @PostMapping("/login")
    public ResponseEntity<JugadorDTO> login(@RequestBody LoginRequest login,
                                        HttpSession session) {  

    /* 1. Buscar jugador */
    Jugador jugador = jugadorRepository.findByNombre(login.getNombre())
        .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));

    /* 2. Validar contraseña */
    if (!BCrypt.checkpw(login.getPassword(), jugador.getPassword())) {
        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Contraseña incorrecta");
    }

    /* 3. Guardar atributos de sesión */
    session.setAttribute("jugador", jugador);
    session.setAttribute("jugadorId", jugador.getId());

    if (jugador.getCaravana() != null) {          
        session.setAttribute("caravanaId", jugador.getCaravana().getId());
    } else {
        session.removeAttribute("caravanaId");
    }
    log.info("🆔 caravanaId en sesión = {}", session.getAttribute("caravanaId"));


    /* 4. OK */
    return ResponseEntity.ok(new JugadorDTO(jugador));
}


@PostMapping("/logout")
public ResponseEntity<Void> logout(HttpSession session) {
    session.invalidate();               // destruye sesión y cookie JSESSIONID
    return ResponseEntity.ok().build(); // 200 OK sin cuerpo
}

    @GetMapping("/me")
public ResponseEntity<AuthResponse> me(HttpSession session) {
    Jugador j = (Jugador) session.getAttribute("jugador");
    if (j == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 sin excepción
    }

    // 🛠 Restaurar atributos si no están
    if (session.getAttribute("jugadorId") == null) {
        session.setAttribute("jugadorId", j.getId());
    }
    if (session.getAttribute("caravanaId") == null) {
        session.setAttribute("caravanaId", j.getCaravana().getId());
    }

    return ResponseEntity.ok(new AuthResponse(j));
}


    @PostMapping("/registrar")
public ResponseEntity<JugadorDTO> registrar(@RequestBody RegistroRequest request,
                                            HttpSession session) {

    /* 1. Nombre único */
    if (jugadorRepository.findByNombre(request.getNombre()).isPresent()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                          "Ese nombre ya está en uso");
    }

    /* 2. Rol a enum (valida) */
    Rol rol;
    try {
        rol = Rol.valueOf(request.getRol().trim().toUpperCase());
    } catch (IllegalArgumentException ex) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol inválido");
    }

    /* 3. Caravana que se asociará al jugador */
    Caravana caravana;

    if (rol == Rol.ADMINISTRADOR) {
        if (request.getNombreCaravana() == null || request.getNombreCaravana().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                              "El administrador debe asignar un nombre a la caravana.");
        }
        caravana = caravanaService.crearCaravanaPorDefecto(request.getNombreCaravana());

    } else { /* CARAVANERO o COMERCIANTE */
        if (request.getCaravanaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                              "Debes seleccionar una caravana para unirte.");
        }
        caravana = caravanaService.findById(request.getCaravanaId());
    }

    /* 4. Persistir jugador */
    Jugador nuevo = jugadorService.crearJugador(
            request.getNombre(), request.getPassword(), rol, caravana);

    /* 5. Crear sesión */
    session.setAttribute("jugador", nuevo);
    session.setAttribute("jugadorId", nuevo.getId());
    session.setAttribute("caravanaId", caravana.getId());  

    log.info("🆔 caravanaId en sesión = {}", session.getAttribute("caravanaId"));

    /* 6. Respuesta */
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new JugadorDTO(nuevo));
}

    
}

