package com.ejemplo.caravana.service;

import org.springframework.stereotype.Service;

import com.ejemplo.caravana.exception.BusinessException;
import com.ejemplo.caravana.model.Jugador;
import com.ejemplo.caravana.model.Rol;

import jakarta.servlet.http.HttpSession;

@Service
public class PermisosService {

    public Jugador getJugadorAutenticado(HttpSession session) {
        Jugador jugador = (Jugador) session.getAttribute("jugador");
        if (jugador == null) {
            throw new BusinessException("No hay jugador autenticado");
        }
        return jugador;
    }

    public void validarAdministrador(HttpSession session) {
        Jugador jugador = getJugadorAutenticado(session);
        if (jugador.getRol() != Rol.ADMINISTRADOR) {
            throw new BusinessException("Acceso restringido al administrador");
        }
    }

    public void validarCaravanero(HttpSession session) {
        Jugador jugador = getJugadorAutenticado(session);
        if (jugador.getRol() != Rol.CARAVANERO) {
            throw new BusinessException("Solo el caravanero puede realizar esta acción");
        }
    }

    public void validarComercianteOCaravanero(HttpSession session) {
        Jugador jugador = getJugadorAutenticado(session);
        if (jugador.getRol() != Rol.CARAVANERO && jugador.getRol() != Rol.COMERCIANTE) {
            throw new BusinessException("No tienes permisos para comerciar");
        }
    }
    public Long obtenerCaravanaIdDesdeSesion(HttpSession session) {
    return getJugadorAutenticado(session).getCaravana().getId();
}

}

