package com.ejemplo.caravana.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ejemplo.caravana.model.Caravana;

public class CaravanaDTO {
    private Long id;
    private String nombre;
    private double velocidad;
    private double capacidadOriginal;
    private double capacidadMax;
    private double cargaActual;
    private double dineroInicial;
    private double dinero;
    private double vida;
    private double metaGanancia;
    private List<RutaDTO> rutasDisponibles;
    private long tiempoTranscurrido;
    private long tiempoLimite;
    private boolean tieneGuardias;
    private boolean haViajado;
    private Long ciudadActualId;
    private String ciudadActualNombre;
    private LocalDateTime ultimaActualizacion;
    private List<Long> itinerario;
    private List<String> serviciosUsados;
    private String estado;





    public CaravanaDTO() {}

    public CaravanaDTO(Caravana c) {
            
        this.id = c.getId();
        this.nombre = c.getNombre();
        this.velocidad = c.getVelocidad();
        this.capacidadOriginal = c.getCapacidadOriginal();
        this.capacidadMax = c.getCapacidadMax();
        this.cargaActual = c.getCargaActual();
        this.dinero = c.getDinero();
        this.vida = c.getVida();
        this.tiempoTranscurrido = c.getTiempoTranscurrido();
        this.tiempoLimite = c.getTiempoLimite();
        this.tieneGuardias = c.isTieneGuardias();
        this.dineroInicial = c.getDineroInicial();  
        this.haViajado = c.isHaViajado();
        this.ciudadActualId = c.getCiudadActualId();
        this.metaGanancia = c.getMetaGanancia();
        this.serviciosUsados = c.getServiciosUsados();
        this.tieneGuardias = c.isTieneGuardias();

        this.estado = c.getEstado();

        this.ultimaActualizacion = c.getUltimaActualizacion();

        this.ciudadActualNombre = (c.getCiudadActual() != null)
            ? c.getCiudadActual().getNombre()
            : null;
        this.itinerario = c.getItinerario();

        
    }
    
    public CaravanaDTO(Caravana c, List<RutaDTO> rutasDisponibles) {
        this(c); // llama al anterior
        this.rutasDisponibles = rutasDisponibles;
    }

    // Getters y setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public List<Long> getItinerario() {
        return itinerario;
    }
    public void setItinerario(List<Long> itinerario) {
        this.itinerario = itinerario;
    }

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

    public boolean isTieneGuardias() { return tieneGuardias; }
    public void setTieneGuardias(boolean tieneGuardias) { this.tieneGuardias = tieneGuardias; }

    public boolean isHaViajado() { return haViajado; }
    public void setHaViajado(boolean haViajado) { this.haViajado = haViajado; }

    public Long getCiudadActualId() { return ciudadActualId; }
    public void setCiudadActualId(Long ciudadActualId) { this.ciudadActualId = ciudadActualId; }

    public String getCiudadActualNombre() { return ciudadActualNombre; }
    public void setCiudadActualNombre(String ciudadActualNombre) { this.ciudadActualNombre = ciudadActualNombre; }

    public LocalDateTime getUltimaActualizacion() { return ultimaActualizacion; }
    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) { this.ultimaActualizacion = ultimaActualizacion; }

    public double getDineroInicial() {return dineroInicial;}    
    public void setDineroInicial(double dineroInicial) {this.dineroInicial = dineroInicial;}

    public double getMetaGanancia() {return metaGanancia;}
    public void setMetaGanancia(double metaGanancia) {this.metaGanancia = metaGanancia;}

    public void setRutasDisponibles(List<RutaDTO> rutasDisponibles) {
        this.rutasDisponibles = rutasDisponibles;
    }

    public List<RutaDTO> getRutasDisponibles() {
        return rutasDisponibles;
    }

    public List<String> getServiciosUsados() {
        return serviciosUsados;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
   
    

    
    
}


