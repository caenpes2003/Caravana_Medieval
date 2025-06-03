package com.ejemplo.caravana.dto;

import jakarta.validation.constraints.NotNull;

public class TravelRequest {
    @NotNull
    private Long rutaId;

    public TravelRequest() {}
    public TravelRequest(Long rutaId) { this.rutaId = rutaId; }

    public Long getRutaId() { return rutaId; }
    public void setRutaId(Long rutaId) { this.rutaId = rutaId; }
}
