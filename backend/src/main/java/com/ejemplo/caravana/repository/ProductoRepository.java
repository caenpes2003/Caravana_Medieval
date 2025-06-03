package com.ejemplo.caravana.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ejemplo.caravana.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto,Long> {}