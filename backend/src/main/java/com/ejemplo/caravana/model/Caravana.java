package com.ejemplo.caravana.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

/**
 * Entidad que representa el estado de la caravana del jugador.
 */
@Entity
public class Caravana {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private double velocidad;
    private double capacidadMax;
    private double capacidadOriginal;    
    private double cargaActual;
    private double dinero;
    private double vida;
    private long tiempoTranscurrido;
    private long tiempoLimite;
    private double metaGanancia;
    @Column(name = "dinero_inicial", nullable = false)
    private double dineroInicial;
    private boolean tieneGuardias;
    private boolean haViajado;
    private LocalDateTime ultimaActualizacion;
    @ElementCollection
    @CollectionTable(name = "caravana_servicios_usados", joinColumns = @JoinColumn(name = "caravana_id"))
    @Column(name = "registro")
    private List<String> serviciosUsados = new ArrayList<>();
    @OneToMany(mappedBy = "caravana", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CiudadCaravana> ciudades = new ArrayList<>();
    @Column(nullable = false)
    private String estado = "EN_ESPERA";



    @Column(name = "ciudad_actual_id")
    private Long ciudadActualId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ciudad_actual_id", insertable = false, updatable = false)
    private Ciudad ciudadActual;

   
    @ElementCollection
    @CollectionTable(name = "caravana_itinerario", joinColumns = @JoinColumn(name = "caravana_id"))
    @Column(name = "ciudad_id")
    private List<Long> itinerario = new ArrayList<>();

    public Caravana() {}

    // constructor existente...
    public Caravana(String nombre,
                    double velocidad,
                    double capacidadOriginal,
                    double dinero,
                    double vida,
                    long tiempoLimite,
                    double metaGanancia) {
        this.nombre = nombre;
        this.velocidad = velocidad;
        this.capacidadOriginal = capacidadOriginal;
        this.capacidadMax = capacidadOriginal;
        this.cargaActual = 0;
        this.dinero = dinero;
        this.vida = vida;
        this.tiempoTranscurrido = 0;
        this.tiempoLimite = tiempoLimite;
        this.metaGanancia = metaGanancia;
        this.tieneGuardias = false;
        this.haViajado = false;
        this.ciudadActualId = null;
        this.estado = "EN_ESPERA";
        
    }

    // Getters y Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public List<Long> getItinerario() {
        return itinerario;
    }
    public void setItinerario(List<Long> itinerario) {
        this.itinerario = itinerario;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getVelocidad() { return velocidad; }
    public void setVelocidad(double velocidad) { this.velocidad = velocidad; }

    public double getCapacidadOriginal() { return capacidadOriginal; }
    public void setCapacidadOriginal(double capacidadOriginal) { this.capacidadOriginal = capacidadOriginal; }

    public double getCapacidadMax() { return capacidadMax; }
    public void setCapacidadMax(double capacidadMax) { this.capacidadMax = capacidadMax; }

    public double getCargaActual() { return cargaActual; }
    public void setCargaActual(double cargaActual) { this.cargaActual = cargaActual; }

    public double getDinero() { return dinero; }
    public void setDinero(double dinero) { this.dinero = dinero; }

    public double getVida() { return vida; }
    public void setVida(double vida) { this.vida = vida; }

    public long getTiempoTranscurrido() { return tiempoTranscurrido; }
    public void setTiempoTranscurrido(long tiempoTranscurrido) { this.tiempoTranscurrido = tiempoTranscurrido; }

    public long getTiempoLimite() { return tiempoLimite; }
    public void setTiempoLimite(long tiempoLimite) { this.tiempoLimite = tiempoLimite; }

    public double getMetaGanancia() { return metaGanancia; }
    public void setMetaGanancia(double metaGanancia) { this.metaGanancia = metaGanancia; }

    public boolean isTieneGuardias() { return tieneGuardias; }
    public void setTieneGuardias(boolean tieneGuardias) { this.tieneGuardias = tieneGuardias; }

    public Long getCiudadActualId() { return ciudadActualId; }
    public void setCiudadActualId(Long ciudadActualId) { this.ciudadActualId = ciudadActualId; }

    public boolean isHaViajado() { return haViajado; }
    public void setHaViajado(boolean haViajado) { this.haViajado = haViajado; }
 
    public Ciudad getCiudadActual() { return ciudadActual; }
    public void setCiudadActual(Ciudad ciudadActual) { this.ciudadActual = ciudadActual;}

    public LocalDateTime getUltimaActualizacion() {return ultimaActualizacion;}    
    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {this.ultimaActualizacion = ultimaActualizacion;}

    public double getDineroInicial() {return dineroInicial;}
    public void setDineroInicial(double dineroInicial) {this.dineroInicial = dineroInicial;}

    public List<String> getServiciosUsados() {return serviciosUsados;}
    
    public void setServiciosUsados(List<String> serviciosUsados) {this.serviciosUsados = serviciosUsados; }
    
    public List<Long> getCiudadIds() {return itinerario;}

    public List<CiudadCaravana> getCiudades() {return ciudades;}

    public void setCiudades(List<CiudadCaravana> ciudades) {this.ciudades = ciudades;}    

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
