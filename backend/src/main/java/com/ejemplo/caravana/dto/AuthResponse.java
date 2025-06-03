package com.ejemplo.caravana.dto;

import com.ejemplo.caravana.model.Jugador;

public class AuthResponse {
    private Long id;
    private String nombre;
    private String rol;

    public AuthResponse(Jugador j) {
        this.id = j.getId();
        this.nombre = j.getNombre();
        this.rol = j.getRol().name();
    }

    // Getters
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getRol() { return rol; }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
