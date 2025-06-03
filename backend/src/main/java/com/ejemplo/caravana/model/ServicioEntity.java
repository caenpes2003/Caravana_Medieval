package com.ejemplo.caravana.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "servicio")
public class ServicioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tipo;
    private double costo;

    @ManyToOne
    @JoinColumn(name = "caravana_id")
    private Caravana caravana;


    public ServicioEntity() {}
    public ServicioEntity(String tipo, double costo, Caravana caravana) {        
        this.tipo = tipo;
        this.costo = costo;
        this.caravana = caravana;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }

    public Caravana getCaravana() {return caravana;}
    public void setCaravana(Caravana caravana) {this.caravana = caravana;}
}