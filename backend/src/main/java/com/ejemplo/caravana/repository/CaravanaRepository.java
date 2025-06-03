package com.ejemplo.caravana.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ejemplo.caravana.model.Caravana;

public interface CaravanaRepository extends JpaRepository<Caravana,Long> {
    Optional<Caravana> findByNombre(String nombre);
    

}