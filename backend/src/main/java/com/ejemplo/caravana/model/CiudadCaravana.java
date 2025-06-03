package com.ejemplo.caravana.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ciudad_caravana")
public class CiudadCaravana {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(name = "impuesto_entrada")
    private double impuestoEntrada;



    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "caravana_id")
    private Caravana caravana;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ciudad_id")
    private Ciudad ciudad;

    protected CiudadCaravana() {}

    public CiudadCaravana(String nombre,
                          double impuestoEntrada,
                          Caravana caravana,
                          Ciudad ciudad) {
        this.nombre          = nombre;
        this.impuestoEntrada = impuestoEntrada;
        this.caravana        = caravana;
        this.ciudad          = ciudad;
    }

    public CiudadCaravana(String nombre,
                          double impuestoEntrada,
                          Caravana caravana) {
        this(nombre, impuestoEntrada, caravana, null);
    }
    
    // Getters y Setters

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getImpuestoEntrada() {
        return impuestoEntrada;
    }

    public Caravana getCaravana() {
        return caravana;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setImpuestoEntrada(double impuestoEntrada) {
        this.impuestoEntrada = impuestoEntrada;
    }

    public void setCaravana(Caravana caravana) {
        this.caravana = caravana;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }
}
