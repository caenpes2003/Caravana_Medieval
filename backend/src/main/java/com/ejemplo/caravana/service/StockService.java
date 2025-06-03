package com.ejemplo.caravana.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ejemplo.caravana.exception.BusinessException;
import com.ejemplo.caravana.exception.ResourceNotFoundException;
import com.ejemplo.caravana.model.Caravana;
import com.ejemplo.caravana.model.Ciudad;
import com.ejemplo.caravana.model.Jugador;
import com.ejemplo.caravana.model.Producto;
import com.ejemplo.caravana.model.Stock;
import com.ejemplo.caravana.repository.CiudadCaravanaRepository;
import com.ejemplo.caravana.repository.CiudadRepository;
import com.ejemplo.caravana.repository.ProductoRepository;
import com.ejemplo.caravana.repository.StockRepository;

import jakarta.servlet.http.HttpSession;
//import com.ejemplo.caravana.service.PermisosService;

@Service
public class StockService {

    private final StockRepository stockRepo;
    private final CiudadRepository ciudadRepo;
    private final ProductoRepository productoRepo;
    private final JugadorService jugadorService;
    private final CiudadCaravanaRepository ciudadCaravanaRepo;
    private final PermisosService permisosSvc;


    public StockService(
        StockRepository stockRepo,
        CiudadRepository ciudadRepo,
        ProductoRepository productoRepo,
        JugadorService jugadorService,
        CiudadCaravanaRepository ciudadCaravanaRepo,
        PermisosService permisosSvc
    ) {
        this.stockRepo = stockRepo;
        this.ciudadRepo = ciudadRepo;
        this.productoRepo = productoRepo;
        this.jugadorService = jugadorService;
        this.ciudadCaravanaRepo = ciudadCaravanaRepo;
        this.permisosSvc = permisosSvc;
    }

    public List<Stock> findByCiudad(Long ciudadId) {
        List<Stock> lista = stockRepo.findByCiudadId(ciudadId);
        if (lista.isEmpty()) {
            throw new ResourceNotFoundException("No hay stock para ciudad " + ciudadId);
        }
        return lista;
    }

    public Stock crear(Long ciudadId, Long productoId, int cantidad, double fd, double fo, HttpSession session) {
    Ciudad ciudad = ciudadRepo.findById(ciudadId)
        .orElseThrow(() -> new ResourceNotFoundException("Ciudad no encontrada: " + ciudadId));

    Jugador jugador = jugadorService.obtenerJugadorDesdeSesion(session);
    Caravana caravana = jugador.getCaravana();

    if (!ciudadPerteneceACaravana(caravana, ciudad.getId())) {
        throw new ResourceNotFoundException("No puedes modificar stock de una ciudad que no está en tu caravana");
    }

    Producto producto = productoRepo.findById(productoId)
        .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + productoId));

    // Validación para evitar duplicado
    if (stockRepo.existsByCiudadIdAndProductoId(ciudadId, productoId)){
    throw new BusinessException("Ya existe stock para este producto en esta ciudad");
    }

    Stock nuevo = new Stock(cantidad, fd, fo, ciudad, producto);
    return stockRepo.save(nuevo);
}

private boolean ciudadPerteneceACaravana(Caravana caravana, Long ciudadId) {
    if (caravana.getCiudades() == null || caravana.getCiudades().isEmpty()) {
        System.out.println("🚨 La caravana no tiene ciudades asociadas (ciudades = null o vacía)");
        return false;
    }

    return caravana.getCiudades().stream()
        .anyMatch(cc -> cc.getCiudad() != null && ciudadId.equals(cc.getCiudad().getId()));
}


    public Stock actualizarPorId(Long stockId, int cantidad, double fd, double fo, HttpSession session) {
    Stock stock = stockRepo.findById(stockId)
        .orElseThrow(() -> new ResourceNotFoundException("Stock no encontrado: " + stockId));

    Ciudad ciudad = stock.getCiudad();
    Jugador jugador = jugadorService.obtenerJugadorDesdeSesion(session);
    Caravana caravana = jugador.getCaravana();

    if (!ciudadPerteneceACaravana(caravana, ciudad.getId())) {
        throw new ResourceNotFoundException("No puedes modificar stock de una ciudad que no está en tu caravana");
    }

    stock.setCantidad(cantidad);
    stock.setFD(fd);
    stock.setFO(fo);
    return stockRepo.save(stock);
}

    @transactional
    public void eliminarPorId(Long stockId, HttpSession session) {

    // 1  Rol admin
        permisosSvc.validarAdministrador(session);

        // 2️  Existe?
        if (!stockRepo.existsById(stockId)) {
            throw new ResourceNotFoundException("Stock no encontrado: " + stockId);
        }

        // 3️  Delete
        stockRepo.deleteById(stockId);
}


public List<Stock> findByCiudadYCaravana(Long ciudadId, Long caravanaId) {
    List<Long> ciudadIds = ciudadCaravanaRepo.findCiudadIdsByCaravanaId(caravanaId);

    if (!ciudadIds.contains(ciudadId)) {
        throw new ResourceNotFoundException("Esta ciudad no pertenece a tu caravana");
    }

    List<Stock> lista = stockRepo.findByCiudadId(ciudadId);

    if (lista.isEmpty()) {
        throw new ResourceNotFoundException("No hay stock para ciudad " + ciudadId);
    }

    return lista;
}



}
