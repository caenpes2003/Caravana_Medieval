package com.ejemplo.caravana.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "servicio_base", uniqueConstraints =
       @UniqueConstraint(columnNames = "tipo"))
public class ServicioBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;
    private double costo;

    public ServicioBase() {}
    public ServicioBase(String tipo, double costo) {
        this.tipo = tipo;
        this.costo = costo;
    }

    /* getters & setters */
    public Long getId() { return id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }
}
