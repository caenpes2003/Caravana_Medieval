package com.ejemplo.caravana.dto;

import com.ejemplo.caravana.model.CiudadCaravanaServicio;

public class ServicioDTO {
    private Long id;
    private String tipo;
    private double costo;

    public ServicioDTO() {
    }

    public ServicioDTO(Long id, String tipo, double costo) {
        this.id = id;
        this.tipo = tipo;
        this.costo = costo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public static ServicioDTO of(CiudadCaravanaServicio ccs) {
        var s = ccs.getServicio(); // ServicioEntity
        return new ServicioDTO(
                s.getId(),
                s.getTipo(),
                s.getCosto());
    }
}
