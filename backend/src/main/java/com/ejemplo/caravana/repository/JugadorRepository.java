package com.ejemplo.caravana.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ejemplo.caravana.model.Jugador;

@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Long> {
    Optional<Jugador> findByNombre(String nombre);
    List<Jugador> findByCaravanaId(Long caravanaId);

}
    
