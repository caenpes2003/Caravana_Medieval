package com.ejemplo.caravana.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ejemplo.caravana.dto.CaravanaDTO;
import com.ejemplo.caravana.dto.CaravanaResumenDTO;
import com.ejemplo.caravana.dto.CommerceRequest;
import com.ejemplo.caravana.dto.JuegoIniciarRequest;
import com.ejemplo.caravana.dto.RutaDTO;
import com.ejemplo.caravana.dto.ServiceRequest;
import com.ejemplo.caravana.dto.StockDTO;
import com.ejemplo.caravana.dto.TravelRequest;
import com.ejemplo.caravana.dto.TravelTaxRequest;
import com.ejemplo.caravana.dto.VictoryDTO;
import com.ejemplo.caravana.exception.VictoryException;
import com.ejemplo.caravana.model.Caravana;
import com.ejemplo.caravana.model.Jugador;
import com.ejemplo.caravana.repository.HistorialRepository;
import com.ejemplo.caravana.repository.JugadorRepository;
import com.ejemplo.caravana.service.CaravanaService;
import com.ejemplo.caravana.service.PermisosService;
import com.ejemplo.caravana.service.RutaService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/caravana")
public class CaravanaController {

    private static final Logger log = LoggerFactory.getLogger(CaravanaController.class);

    private final CaravanaService caravanaService;
    private final RutaService rutaService;
    private final PermisosService permisosService;
    private final JugadorRepository jugadorRepository;
   
 

    public CaravanaController(CaravanaService caravanaService,
            RutaService rutaService,
            PermisosService permisosService,
            JugadorRepository jugadorRepository,
            HistorialRepository historialRepository
           ) {
        this.caravanaService = caravanaService;
        this.rutaService = rutaService;
        this.permisosService = permisosService;
        this.jugadorRepository = jugadorRepository;
       

    }

    /*
     * ────────────────────────────────────────────────────────────────
     * GET /api/caravana → estado actual
     * Devuelve 204 si la partida aún no inicia
     * ────────────────────────────────────────────────────────────────
     */
    @GetMapping
    public ResponseEntity<CaravanaDTO> getEstado(HttpSession session) {

        Caravana c = caravanaService.getEstado(); // nunca null

        /* Partida sin iniciar: ciudadActualId == null */
        if (c.getCiudadActualId() == null) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        /* Rutas saliendo de la ciudad actual */
        List<RutaDTO> rutas = rutaService.findRutasDesdeCiudadActual()
                .stream()
                .map(r -> new RutaDTO(
                        r.getId(),
                        r.getCiudadOrigen().getId(), r.getCiudadOrigen().getNombre(),
                        r.getCiudadDestino().getId(), r.getCiudadDestino().getNombre(),
                        r.getDistancia(), r.isSegura(), r.getDano(), r.getTipoDanio(),
                        r.getCiudadDestino().getImpuestoEntrada(),
                        r.getTiempo(), r.getVelocidad()))
                .toList();

        return ResponseEntity.ok(new CaravanaDTO(c, rutas));
    }

    /** POST /api/caravana/iniciar → iniciar partida */
    @PostMapping("/iniciar")
    public CaravanaDTO iniciarPartida(@RequestBody JuegoIniciarRequest req, HttpSession session) {
        log.info("→ Iniciar partida - caravanaId en sesión: {}", session.getAttribute("caravanaId"));
        log.info("→ Parámetros recibidos: {}", req);

        return new CaravanaDTO(caravanaService.iniciarPartida(req));
    }

    /** POST /api/caravana/impuestos → paga impuestos */
    @PostMapping("/impuestos")
    public CaravanaDTO pagarImpuestos(@RequestBody TravelTaxRequest req) {
        log.info("POST /api/caravana/impuestos → rutaId={}", req.getRutaId());
        Caravana c = caravanaService.pagarImpuestos(req.getRutaId());
        return new CaravanaDTO(c);
    }

    /** POST /api/caravana/viajar → realiza el viaje */
    @PostMapping("/viajar")
    public CaravanaDTO viajar(@RequestBody TravelRequest req, HttpSession session) {
        permisosService.validarCaravanero(session);
        Caravana c = caravanaService.viajar(req.getRutaId());
        return new CaravanaDTO(c);
    }

    /** POST /api/caravana/comprar → compra productos */
    @PostMapping("/comprar")
    public CaravanaDTO comprar(@RequestBody CommerceRequest req, HttpSession session) {
        permisosService.validarComercianteOCaravanero(session);
        Caravana c = caravanaService.comprar(req.getCiudadId(), req.getProductoId(), req.getCantidad());
        return new CaravanaDTO(c);
    }

    /** POST /api/caravana/vender → vende productos */
    @PostMapping("/vender")
    public CaravanaDTO vender(@RequestBody CommerceRequest req, HttpSession session) {
        permisosService.validarComercianteOCaravanero(session);
        Caravana c = caravanaService.vender(req.getCiudadId(), req.getProductoId(), req.getCantidad());
        return new CaravanaDTO(c);
    }

    /** POST /api/caravana/servicios → usa servicio en ciudad */
    @PostMapping("/servicios")
    public CaravanaDTO usarServicio(@RequestBody ServiceRequest req, HttpSession session) {
        permisosService.validarCaravanero(session);
        Caravana c = caravanaService.usarServicio(req.getServicioId(), req.getCiudadId());
        return new CaravanaDTO(c);
    }

    /** GET /api/caravana/inventario → inventario de la caravana */
    @GetMapping("/inventario")
    public List<StockDTO> inventario() {
        return caravanaService.obtenerInventario();
    }
     @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reset(HttpSession session) {
        caravanaService.reset(session);          
    }

    /** Manejo de excepción para victoria */
    @ExceptionHandler(VictoryException.class)
    public VictoryDTO handleVictory(VictoryException ex) {
        Caravana c = caravanaService.getEstado();
        CaravanaDTO dto = new CaravanaDTO(c);
        return new VictoryDTO(dto, "¡Victoria! Has alcanzado la meta.");
    }

    @GetMapping("/disponibles")
    public List<CaravanaResumenDTO> listarCaravanasDisponibles() {
        return caravanaService.findAll()
                .stream()
                .map(c -> new CaravanaResumenDTO(c.getId(), c.getNombre()))
                .toList();
    }

    @GetMapping("/jugadores")
    public List<Map<String, String>> getJugadoresConectados(HttpSession session) {
        Long caravanaId = (Long) session.getAttribute("caravanaId");
        List<Jugador> jugadores = jugadorRepository.findByCaravanaId(caravanaId);

        return jugadores.stream()
                .map(j -> {
                    Map<String, String> info = new HashMap<>();
                    info.put("nombre", j.getNombre());
                    info.put("rol", j.getRol().name());
                    return info;
                })
                .collect(Collectors.toList());
    }

}
