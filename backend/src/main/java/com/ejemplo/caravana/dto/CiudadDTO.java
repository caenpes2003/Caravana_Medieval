package com.ejemplo.caravana.dto;

public class CiudadDTO {
    private Long id;
    private String nombre;
    private double impuestos;

    public CiudadDTO() {}

    public CiudadDTO(Long id, String nombre, double impuestos) {
        this.id = id;
        this.nombre = nombre;
        this.impuestos = impuestos;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getImpuestos() { return impuestos; }
    public void setImpuestos(double impuestos) { this.impuestos = impuestos; }
}
