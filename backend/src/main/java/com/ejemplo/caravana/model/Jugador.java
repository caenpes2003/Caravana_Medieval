package com.ejemplo.caravana.model;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;



@Entity
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @ManyToOne
    private Caravana caravana;

    private String password;
    public Jugador() {
    }

    // Constructor para pruebas o inicialización
    public Jugador(String nombre, String password, Rol rol, Caravana caravana) {
        this.nombre = nombre;
        this.password = password;
        this.rol = rol;
        this.caravana = caravana;
}


    // getters, setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Caravana getCaravana() {
        return caravana;
    }

    public void setCaravana(Caravana caravana) {
        this.caravana = caravana;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
