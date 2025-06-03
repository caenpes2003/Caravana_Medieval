package com.ejemplo.caravana.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ejemplo.caravana.model.Ciudad;
import com.ejemplo.caravana.model.Producto;
import com.ejemplo.caravana.model.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {

    // Lista todo el stock de una ciudad
    List<Stock> findByCiudadId(Long ciudadId);

    // Devuelve **un** registro de stock concreto (opcional)
    Optional<Stock> findByCiudadIdAndProductoId(Long ciudadId, Long productoId);
    boolean existsByCiudadAndProducto(Ciudad ciudad, Producto producto);
    boolean existsByCiudadIdAndProductoId(Long ciudadId, Long productoId);


}
