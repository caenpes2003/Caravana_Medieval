package com.ejemplo.caravana.dto;

public class RegistroRequest {
    private String nombre;
    private String password;
    private String rol;
    private String nombreCaravana;
    private Long caravanaId;
public Long getCaravanaId() { return caravanaId; }
public void setCaravanaId(Long caravanaId) { this.caravanaId = caravanaId; }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getNombreCaravana() {
        return nombreCaravana;
    }

    public void setNombreCaravana(String nombreCaravana) {
        this.nombreCaravana = nombreCaravana;
    }
}
