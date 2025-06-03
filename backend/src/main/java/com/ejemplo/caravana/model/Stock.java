package com.ejemplo.caravana.model;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
@Entity
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int cantidad;
    private double FD;
    private double FO;
    @ManyToOne
    @JoinColumn(name = "ciudad_id")
    private Ciudad ciudad;
    @ManyToOne
    @JoinColumn(name = "producto_id")
    @JsonBackReference
    private Producto producto;

    public Stock() {}
    public Stock(int cantidad, double FD, double FO, Ciudad ciudad, Producto producto) {
        this.cantidad = cantidad;
        this.FD = FD;
        this.FO = FO;
        this.ciudad = ciudad;
        this.producto = producto;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getFD() { return FD; }
    public void setFD(double FD) { this.FD = FD; }
    public double getFO() { return FO; }
    public void setFO(double FO) { this.FO = FO; }
    public Ciudad getCiudad() { return ciudad; }
    public void setCiudad(Ciudad ciudad) { this.ciudad = ciudad; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}