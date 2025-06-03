package com.ejemplo.caravana.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "historial")
public class Historial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false, length = 500)
    private String tipo;

    @Column(nullable = false, length = 500)
    private String detalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caravana_id", nullable = false)
    private Caravana caravana;

    public Historial() {}

    /**
     * Constructor completo para crear registros de historial en un paso.
     */
    public Historial(Long id,
                     LocalDateTime timestamp,
                     String tipo,
                     String detalle,
                     Caravana caravana) {
        this.id        = id;
        this.timestamp = timestamp;
        this.tipo      = tipo;
        this.detalle   = detalle;
        this.caravana  = caravana;
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

    public Caravana getCaravana() { return caravana; }
    public void setCaravana(Caravana caravana) { this.caravana = caravana; }
}
