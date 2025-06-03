package com.ejemplo.caravana.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "ciudad_caravana_servicio",
       uniqueConstraints = @UniqueConstraint(columnNames = {"ciudad_caravana_id","servicio_id"}))
public class CiudadCaravanaServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "ciudad_caravana_id", nullable = false)
    private CiudadCaravana ciudadCaravana;

    @ManyToOne @JoinColumn(name = "servicio_id", nullable = false)
    private ServicioEntity servicio;

    /* ─── constructores ─── */
    public CiudadCaravanaServicio() {}
    public CiudadCaravanaServicio(CiudadCaravana cc, ServicioEntity srv) {
        this.ciudadCaravana = cc;
        this.servicio = srv;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CiudadCaravana getCiudadCaravana() {
        return ciudadCaravana;
    }

    public void setCiudadCaravana(CiudadCaravana ciudadCaravana) {
        this.ciudadCaravana = ciudadCaravana;
    }

    public ServicioEntity getServicio() {
        return servicio;
    }

    public void setServicio(ServicioEntity servicio) {
        this.servicio = servicio;
    }

}
