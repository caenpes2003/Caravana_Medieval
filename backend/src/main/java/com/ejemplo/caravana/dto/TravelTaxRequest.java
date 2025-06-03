package com.ejemplo.caravana.dto;

/**
 * DTO para pagar impuestos antes de viajar.
 */
public class TravelTaxRequest {
    private Long rutaId;

    public TravelTaxRequest() {}

    public TravelTaxRequest(Long rutaId) {
        this.rutaId = rutaId;
    }

    public Long getRutaId() {
        return rutaId;
    }

    public void setRutaId(Long rutaId) {
        this.rutaId = rutaId;
    }
}
