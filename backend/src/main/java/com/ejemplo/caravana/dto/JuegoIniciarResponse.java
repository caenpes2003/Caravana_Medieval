package com.ejemplo.caravana.dto;

import java.util.List;

public class JuegoIniciarResponse {

    private Long caravanaId;
    private List<Long> ciudadIds;        
    private String ciudadInicialNombre;

    private long tiempoLimite;
    private long oroInicial;
    private long metaMonedas;
    private long vidaInicial;

    /* ──────────── constructores ──────────── */
    public JuegoIniciarResponse() {}

    public JuegoIniciarResponse(
            Long caravanaId,
            List<Long> ciudadIds,
            String ciudadInicialNombre,
            long tiempoLimite,
            long oroInicial,
            long metaMonedas,
            long vidaInicial
    ) {
        this.caravanaId = caravanaId;
        this.ciudadIds = ciudadIds;
        this.ciudadInicialNombre = ciudadInicialNombre;
        this.tiempoLimite = tiempoLimite;
        this.oroInicial = oroInicial;
        this.metaMonedas = metaMonedas;
        this.vidaInicial = vidaInicial;
    }

    /* ──────────── getters & setters ──────────── */
    public Long getCaravanaId() { return caravanaId; }
    public void setCaravanaId(Long caravanaId) { this.caravanaId = caravanaId; }

    public List<Long> getCiudadIds() { return ciudadIds; }
    public void setCiudadIds(List<Long> ciudadIds) { this.ciudadIds = ciudadIds; }

    public String getCiudadInicialNombre() { return ciudadInicialNombre; }
    public void setCiudadInicialNombre(String ciudadInicialNombre) {
        this.ciudadInicialNombre = ciudadInicialNombre;
    }

    public long getTiempoLimite() { return tiempoLimite; }
    public void setTiempoLimite(long tiempoLimite) { this.tiempoLimite = tiempoLimite; }

    public long getOroInicial() { return oroInicial; }
    public void setOroInicial(long oroInicial) { this.oroInicial = oroInicial; }

    public long getMetaMonedas() { return metaMonedas; }
    public void setMetaMonedas(long metaMonedas) { this.metaMonedas = metaMonedas; }

    public long getVidaInicial() { return vidaInicial; }
    public void setVidaInicial(long vidaInicial) { this.vidaInicial = vidaInicial; }
}
