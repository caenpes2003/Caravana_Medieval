package com.ejemplo.caravana.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double distancia;
    private boolean segura;
    private double dano;
    private String tipoDanio;

    @ManyToOne
    @JoinColumn(name = "ciudad_origen_id")
    private CiudadCaravana ciudadOrigen;          

    @ManyToOne
    @JoinColumn(name = "ciudad_destino_id")
    private CiudadCaravana ciudadDestino;         

    @Column(name = "impuesto_destino")
    private double impuestoDestino;

    private double velocidad;
    @Column(name = "tiempo")
    private double tiempo; 


    public Ruta() {}

    public Ruta(double distancia, boolean segura, double dano, double impuestoDestino,
                CiudadCaravana origen, CiudadCaravana destino) {
        this.distancia = distancia;
        this.segura = segura;
        this.dano = dano;
        this.impuestoDestino = impuestoDestino;
        this.ciudadOrigen = origen;
        this.ciudadDestino = destino;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getDistancia() { return distancia; }
    public void setDistancia(double distancia) { this.distancia = distancia; }

    public boolean isSegura() { return segura; }
    public void setSegura(boolean segura) { this.segura = segura; }

    public double getDano() { return dano; }
    public void setDano(double dano) { this.dano = dano; }

    public String getTipoDanio() { return tipoDanio; }
    public void setTipoDanio(String tipoDanio) { this.tipoDanio = tipoDanio; }

    public double getImpuestoDestino() { return impuestoDestino; }
    public void setImpuestoDestino(double impuestoDestino) { this.impuestoDestino = impuestoDestino; }

    public double getVelocidad() {return velocidad;}
    public void setVelocidad(double velocidad) {this.velocidad = velocidad;}

    public double getTiempo() {return tiempo;}
    public void setTiempo(double tiempo) {this.tiempo = tiempo;}

    public CiudadCaravana getCiudadDestino() {
        return ciudadDestino;
    }

    public void setCiudadDestino(CiudadCaravana ciudadDestino) {
        this.ciudadDestino = ciudadDestino;
    }

    public CiudadCaravana getCiudadOrigen() {
        return ciudadOrigen;
    }

    public void setCiudadOrigen(CiudadCaravana ciudadOrigen) {
        this.ciudadOrigen = ciudadOrigen;
    }
}
