package com.ejemplo.caravana.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ejemplo.caravana.dto.StockDTO;
import com.ejemplo.caravana.model.Stock;
import com.ejemplo.caravana.service.PermisosService;
import com.ejemplo.caravana.service.StockService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/stock")
public class StockCrudController {

    private final StockService stockService;
    private final PermisosService permisosService;

    public StockCrudController(StockService stockService, PermisosService permisosService) {
        this.stockService = stockService;
        this.permisosService = permisosService;
    }

    /** ✅ PUT /api/stock/{stockId} → Actualizar stock directamente por ID */
    @PutMapping("/{stockId}")
    public ResponseEntity<StockDTO> actualizarStock(
        @PathVariable Long stockId,
        @RequestBody StockDTO dto,
        HttpSession session) {

        permisosService.validarAdministrador(session);
        Stock actualizado = stockService.actualizarPorId(stockId, dto.getCantidad(), dto.getFD(), dto.getFO(), session);


        return ResponseEntity.ok(new StockDTO(
            actualizado.getId(),
            actualizado.getProducto().getId(),
            actualizado.getProducto().getNombre(),
            actualizado.getCantidad(),
            actualizado.getFD(),
            actualizado.getFO()
        ));
    }

    /** ❌ DELETE /api/stock/{stockId} → Eliminar stock directamente por ID */
    @DeleteMapping("/{stockId}")
public ResponseEntity<Void> eliminarStock(@PathVariable Long stockId,
                                          HttpSession session) {

    permisosService.validarAdministrador(session);

    /* 1.  Verificamos que el stock existe y pertenece a la caravana en sesión */
    stockService.eliminarPorId(stockId, session);

    /* 2. 204  No Content  */
    return ResponseEntity.noContent().build();
}
}
