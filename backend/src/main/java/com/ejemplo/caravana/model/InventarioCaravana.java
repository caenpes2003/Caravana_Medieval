package com.ejemplo.caravana.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class InventarioCaravana {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "caravana_id", nullable = false)
    private Caravana caravana;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    private int cantidad;

    public InventarioCaravana() {}

    public InventarioCaravana(Caravana caravana, Producto producto, int cantidad) {
        this.caravana = caravana;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Caravana getCaravana() { return caravana; }
    public void setCaravana(Caravana caravana) { this.caravana = caravana; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
