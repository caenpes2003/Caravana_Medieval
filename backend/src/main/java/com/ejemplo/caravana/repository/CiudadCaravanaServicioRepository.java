package com.ejemplo.caravana.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ejemplo.caravana.model.CiudadCaravana;
import com.ejemplo.caravana.model.CiudadCaravanaServicio;

import jakarta.transaction.Transactional;

@Repository
public interface CiudadCaravanaServicioRepository
        extends JpaRepository<CiudadCaravanaServicio, Long> {

    List<CiudadCaravanaServicio> findByCiudadCaravanaId(Long ciudadCaravanaId);
    @Modifying @Transactional
    void deleteByServicio_Id(Long servicioId);

          
    void deleteByCiudadCaravana(CiudadCaravana ciudadCaravana);
    @Modifying
    @Transactional
    @Query("""
           delete from CiudadCaravanaServicio ccs
           where ccs.ciudadCaravana.ciudad.id = :ciudadId
           """)
    void deleteByCiudadBase(@Param("ciudadId") Long ciudadId);
    List<CiudadCaravanaServicio>
        findByCiudadCaravana_Ciudad_Id(Long idBase);
   
}
