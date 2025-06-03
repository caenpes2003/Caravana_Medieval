package com.ejemplo.caravana.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ejemplo.caravana.model.Caravana;
import com.ejemplo.caravana.model.Ciudad;
import com.ejemplo.caravana.model.CiudadCaravana;

import jakarta.transaction.Transactional;

public interface CiudadCaravanaRepository extends JpaRepository<CiudadCaravana, Long> {

    /* --- métodos que el servicio reclama --- */
    List<CiudadCaravana> findByCaravana(Caravana caravana);

    Optional<CiudadCaravana> findByIdAndCaravana(Long id, Caravana caravana);

    
    @Query("""
           SELECT cc.ciudad.id
           FROM   CiudadCaravana cc
           WHERE  cc.caravana.id = :caravanaId
           """)
    List<Long> findCiudadIdsByCaravanaId(Long caravanaId);
    boolean existsByCaravanaAndCiudad(Caravana caravana, Ciudad ciudad);

    @Query("""
           SELECT COUNT(cc) > 0
           FROM   CiudadCaravana cc
           WHERE  cc.ciudad.id = :ciudadId
           AND    cc.caravana.estado <> 'EN_ESPERA'
           """)
    boolean existsCiudadVinculadaEnPartida (@Param("ciudadId") Long ciudadId);

    @Modifying @Transactional
       void deleteByCiudad_Id(Long ciudadBaseId); 
       Optional<CiudadCaravana> findByCaravanaAndNombre(Caravana caravana, String nombre);  


}
