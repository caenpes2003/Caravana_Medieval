package com.ejemplo.caravana.dto;

/**
 * DTO para solicitar uso de servicio en una ciudad concreta.
 */
public class ServiceRequest {
    private Long servicioId;
    private Long ciudadId;

    public ServiceRequest() {}

    public ServiceRequest(Long servicioId, Long ciudadId) {
        this.servicioId = servicioId;
        this.ciudadId = ciudadId;
    }

    public Long getServicioId() {
        return servicioId;
    }

    public void setServicioId(Long servicioId) {
        this.servicioId = servicioId;
    }

    public Long getCiudadId() {
        return ciudadId;
    }

    public void setCiudadId(Long ciudadId) {
        this.ciudadId = ciudadId;
    }
}
