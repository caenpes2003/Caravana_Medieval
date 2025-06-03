package com.ejemplo.caravana.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ejemplo.caravana.dto.StockDTO;
import com.ejemplo.caravana.service.PermisosService;
import com.ejemplo.caravana.service.StockService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/ciudades/{ciudadId}/stock")
public class StockController {

    private final StockService stockService;
    private final PermisosService permisosService;

    public StockController(StockService stockService, PermisosService permisosService) {
        this.stockService = stockService;
        this.permisosService = permisosService;
    }

    /** ✅ GET /api/ciudades/{ciudadId}/stock → listar stock por ciudad */
    @GetMapping
public List<StockDTO> getStockPorCiudad(@PathVariable Long ciudadId, HttpSession session) {
    Long caravanaId = permisosService.getJugadorAutenticado(session).getCaravana().getId();

    return stockService.findByCiudadYCaravana(ciudadId, caravanaId).stream()
        .map(s -> new StockDTO(
            s.getId(),
            s.getProducto().getId(),
            s.getProducto().getNombre(),
            s.getCantidad(),
            s.getFD(),
            s.getFO()
        ))
        .collect(Collectors.toList());
}


    /** ✅ POST /api/ciudades/{ciudadId}/stock → crear nuevo stock */
    @PostMapping
    public ResponseEntity<StockDTO> crearStock(
        @PathVariable Long ciudadId,
        @RequestBody StockDTO dto,
        HttpSession session) {

        permisosService.validarAdministrador(session);

        stockService.crear(ciudadId, dto.getProductoId(), dto.getCantidad(), dto.getFD(), dto.getFO(), session);
        return ResponseEntity.ok(new StockDTO(
            null, // ID se asigna al crear
            dto.getProductoId(),
            dto.getProductoNombre(),
            dto.getCantidad(),
            dto.getFD(),
            dto.getFO()
        ));

      
    }
}
