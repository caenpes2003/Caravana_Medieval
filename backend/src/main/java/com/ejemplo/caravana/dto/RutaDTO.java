package com.ejemplo.caravana.dto;

import java.util.List;

import com.ejemplo.caravana.model.Ruta;

public class RutaDTO {
    private Long id;
    private Long ciudadOrigenId;
    private String ciudadOrigenNombre;
    private Long ciudadDestinoId;
    private String ciudadDestinoNombre;
    private double distancia;
    private boolean segura;
    private double dano;
    private String tipoDanio;
    private double impuestoDestino;
    private List<RutaDTO> rutasDisponibles;
    private double tiempo;
    private double velocidad;

    public RutaDTO() {}

    public RutaDTO(Long id,
                   Long ciudadOrigenId,
                   String ciudadOrigenNombre,
                   Long ciudadDestinoId,
                   String ciudadDestinoNombre,
                   double distancia,
                   boolean segura,
                   double dano,
                   String tipoDanio,
                   double impuestoDestino,
                   double tiempo,
                   double velocidad) {
        this.id = id;
        this.ciudadOrigenId = ciudadOrigenId;
        this.ciudadOrigenNombre = ciudadOrigenNombre;
        this.ciudadDestinoId = ciudadDestinoId;
        this.ciudadDestinoNombre = ciudadDestinoNombre;
        this.distancia = distancia;
        this.segura = segura;
        this.dano = dano;
        this.tipoDanio = tipoDanio;
        this.impuestoDestino = impuestoDestino;
        this.tiempo = tiempo;
        this.velocidad = velocidad;
    }

    // Constructor que recibe un objeto Ruta (para facilitar conversiones)
    public RutaDTO(Ruta r) {
        this.id = r.getId();
        this.ciudadOrigenId = r.getCiudadOrigen().getId();
        this.ciudadOrigenNombre = r.getCiudadOrigen().getNombre();
        this.ciudadDestinoId = r.getCiudadDestino().getId();
        this.ciudadDestinoNombre = r.getCiudadDestino().getNombre();
        this.distancia = r.getDistancia();
        this.segura = r.isSegura();
        this.dano = r.getDano();
        this.tipoDanio = r.getTipoDanio();
        this.impuestoDestino = r.getImpuestoDestino();
        this.tiempo = r.getTiempo();
        this.velocidad = r.getVelocidad();
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCiudadOrigenId() { return ciudadOrigenId; }
    public void setCiudadOrigenId(Long ciudadOrigenId) { this.ciudadOrigenId = ciudadOrigenId; }

    public Long getCiudadDestinoId() { return ciudadDestinoId; }
    public void setCiudadDestinoId(Long ciudadDestinoId) { this.ciudadDestinoId = ciudadDestinoId; }

    public String getCiudadOrigenNombre() { return ciudadOrigenNombre; }
    public void setCiudadOrigenNombre(String ciudadOrigenNombre) { this.ciudadOrigenNombre = ciudadOrigenNombre; }

    public String getCiudadDestinoNombre() { return ciudadDestinoNombre; }
    public void setCiudadDestinoNombre(String ciudadDestinoNombre) { this.ciudadDestinoNombre = ciudadDestinoNombre; }

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

    public List<RutaDTO> getRutasDisponibles() { return rutasDisponibles; }
    public void setRutasDisponibles(List<RutaDTO> rutasDisponibles) { this.rutasDisponibles = rutasDisponibles; }

    public double getTiempo() { return tiempo; }
    public void setTiempo(double tiempo) { this.tiempo = tiempo; }

    public double getVelocidad() { return velocidad; }
    public void setVelocidad(double velocidad) { this.velocidad = velocidad; }
}
