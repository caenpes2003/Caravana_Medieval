package com.ejemplo.caravana.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.ejemplo.caravana.exception.ResourceNotFoundException;
import com.ejemplo.caravana.model.Caravana;
import com.ejemplo.caravana.model.Jugador;
import com.ejemplo.caravana.model.Rol;
import com.ejemplo.caravana.repository.JugadorRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class JugadorService {

    private final JugadorRepository jugadorRepository;

    @Autowired
    public JugadorService(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    /**
     * Crea un nuevo jugador, encriptando su contraseña con BCrypt antes de guardar.
     */
    public Jugador crearJugador(String nombre, String passwordPlano, Rol rol, Caravana caravana) {
        String hashedPassword = BCrypt.hashpw(passwordPlano, BCrypt.gensalt());
        Jugador jugador = new Jugador(nombre, hashedPassword, rol, caravana);
        return jugadorRepository.save(jugador);
    }

    /**
     * Buscar un jugador por nombre en la base de datos.
     */
    public Jugador buscarPorNombre(String nombre) {
        return jugadorRepository.findByNombre(nombre).orElse(null);
    }
    public Jugador obtenerJugadorDesdeSesion(HttpSession session) {
    Long jugadorId = (Long) session.getAttribute("jugadorId");
    if (jugadorId == null) {
        throw new IllegalStateException("Sesión inválida o no autenticada");
    }

    return jugadorRepository.findById(jugadorId)
        .orElseThrow(() -> new ResourceNotFoundException("Jugador no encontrado: " + jugadorId));
}

}
