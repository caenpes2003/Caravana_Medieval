package com.ejemplo.caravana.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ejemplo.caravana.model.InventarioCaravana;

public interface InventarioCaravanaRepository extends JpaRepository<InventarioCaravana, Long> {
    List<InventarioCaravana> findByCaravanaId(Long caravanaId);
    Optional<InventarioCaravana> findByCaravanaIdAndProductoId(Long caravanaId, Long productoId);
}
 