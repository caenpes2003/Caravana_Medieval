package com.ejemplo.caravana.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CommerceRequest {
    @NotNull
    private Long ciudadId;

    @NotNull
    private Long productoId;

    @Min(1)
    private int cantidad;

    public CommerceRequest() {}

    public CommerceRequest(Long ciudadId, Long productoId, int cantidad) {
        this.ciudadId   = ciudadId;
        this.productoId = productoId;
        this.cantidad   = cantidad;
    }

    public Long getCiudadId() { return ciudadId; }
    public void setCiudadId(Long ciudadId) { this.ciudadId = ciudadId; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
