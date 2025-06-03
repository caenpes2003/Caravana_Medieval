package com.ejemplo.caravana.config;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Filtro personalizado que protege rutas privadas validando si hay sesión activa.
 */
@Component
public class AuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,  
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // 🔓 Rutas públicas permitidas sin sesión
        if (
            path.startsWith("/api/auth") ||
            path.equals("/api/caravana/disponibles") ||
            path.startsWith("/h2-console") // ✅ permitimos acceso libre a la consola H2
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔐 Validar sesión activa para el resto de rutas
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("jugador") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Debe iniciar sesión");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
