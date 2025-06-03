package com.ejemplo.caravana.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ejemplo.caravana.model.Ruta;

import jakarta.transaction.Transactional;

public interface RutaRepository extends JpaRepository<Ruta, Long> {

    List<Ruta> findByCiudadOrigen_Id(Long ciudadCaravanaId);

    List<Ruta> findByCiudadOrigen_IdAndCiudadDestino_Id(Long origenId, Long destinoId);

    List<Ruta> findByCiudadOrigen_IdAndSeguraTrue(Long ciudadCaravanaId);

    List<Ruta> findByCiudadOrigen_IdAndSeguraFalse(Long ciudadCaravanaId);

    /* --- NUEVO: borrado seguro por caravana --- */
    @Modifying
    @Transactional
    @Query("""
        delete from Ruta r
        where r.ciudadOrigen.caravana.id = :caravanaId
           or r.ciudadDestino.caravana.id = :caravanaId
    """)
    void deleteByCaravanaId(@Param("caravanaId") Long caravanaId);
}

