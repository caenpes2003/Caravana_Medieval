package com.ejemplo.caravana.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ejemplo.caravana.model.ServicioEntity;

public interface ServicioRepository extends JpaRepository<ServicioEntity, Long> {
    List<ServicioEntity> findByCaravanaId(Long caravanaId);
    @Query("""
           SELECT COUNT(cs) > 0
           FROM   CiudadCaravanaServicio cs
           WHERE  cs.servicio.id = :servicioId
           """)
    boolean estaVinculado(@Param("servicioId") Long servicioId);
}
