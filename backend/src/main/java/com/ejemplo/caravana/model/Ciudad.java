package com.ejemplo.caravana.model;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "ciudad",
    uniqueConstraints = @UniqueConstraint(name = "uk_ciudad_nombre", columnNames = "nombre")
)
public class Ciudad {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nombre;

  @Column(name = "impuesto_entrada")
  private double impuestoEntrada;

 
  @OneToMany(mappedBy = "ciudad", cascade = CascadeType.ALL)
  @JsonManagedReference          
  private List<Stock> stocks;
  
  public Ciudad() {}
    public Ciudad(String nombre, double impuestoEntrada) {
        this.nombre = nombre;
        this.impuestoEntrada = impuestoEntrada;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getImpuestoEntrada() { return impuestoEntrada; }
    public void setImpuestoEntrada(double impuestoEntrada) { this.impuestoEntrada = impuestoEntrada; }

    public List<Stock> getStocks() { return stocks; }
    public void setStocks(List<Stock> stocks) { this.stocks = stocks; }

}