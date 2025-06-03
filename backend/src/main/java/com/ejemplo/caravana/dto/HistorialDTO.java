package com.ejemplo.caravana.dto;

import java.time.LocalDateTime;

public class HistorialDTO {
    private Long id;
    private LocalDateTime timestamp;
    private String tipo;
    private String detalle;

    public HistorialDTO(Long id, LocalDateTime timestamp, String tipo, String detalle) {
        this.id = id;
        this.timestamp = timestamp;
        this.tipo = tipo;
        this.detalle = detalle;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }
}