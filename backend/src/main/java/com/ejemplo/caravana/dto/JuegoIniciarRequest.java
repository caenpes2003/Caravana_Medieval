package com.ejemplo.caravana.dto;

import java.util.List;

public class JuegoIniciarRequest {

    private List<Long> ciudadIds;
    private long tiempoLimite;
    private long oroInicial;
    private long metaMonedas;
    private long vidaInicial;
    private long monedasIniciales;

    public JuegoIniciarRequest() {}

    public List<Long> getCiudadIds() {return ciudadIds;}
    public void setCiudadIds(List<Long> ciudadIds) {this.ciudadIds = ciudadIds;}

    public long getTiempoLimite() {return tiempoLimite;}
    public void setTiempoLimite(long tiempoLimite) {this.tiempoLimite = tiempoLimite;}

    public long getOroInicial() {return oroInicial;}
    public void setOroInicial(long oroInicial) {this.oroInicial = oroInicial;}

    public long getMonedasIniciales() { return monedasIniciales;}
    public void setMonedasIniciales(long monedasIniciales) { this.monedasIniciales = monedasIniciales; }

    public long getMetaMonedas() { return metaMonedas; }
    public void setMetaMonedas(long metaMonedas) { this.metaMonedas = metaMonedas; }

    public long getVidaInicial() {return vidaInicial;}
    public void setVidaInicial(long vidaInicial) {this.vidaInicial = vidaInicial;}

    
}
