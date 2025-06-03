package com.ejemplo.caravana.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ejemplo.caravana.model.Ciudad;

@Repository
public interface CiudadRepository extends JpaRepository<Ciudad, Long> {
    Optional<Ciudad> findByNombreIgnoreCase(String nombre);

}
