package com.ejemplo.caravana.dto;

import com.ejemplo.caravana.model.Jugador;

public class JugadorDTO {
    private Long id;
    private String nombre;
    private String rol;

    public JugadorDTO(Jugador jugador) {
        this.id = jugador.getId();
        this.nombre = jugador.getNombre();
        this.rol = jugador.getRol().name();
    }

    // getters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
