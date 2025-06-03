package com.ejemplo.caravana.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ejemplo.caravana.model.Historial;

import jakarta.transaction.Transactional;

public interface HistorialRepository extends JpaRepository<Historial, Long> {
    List<Historial> findByCaravanaIdOrderByTimestampDesc(Long caravanaId);
    @Transactional
    void deleteByCaravanaId(Long caravanaId);
}