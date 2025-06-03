package com.ejemplo.caravana.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ejemplo.caravana.dto.AsociarCiudadRequest;
import com.ejemplo.caravana.dto.CiudadDTO;
import com.ejemplo.caravana.dto.ServicioDTO;
import com.ejemplo.caravana.exception.ResourceNotFoundException;
import com.ejemplo.caravana.model.Caravana;
import com.ejemplo.caravana.model.Ciudad;
import com.ejemplo.caravana.model.CiudadCaravana;
import com.ejemplo.caravana.model.Jugador;
import com.ejemplo.caravana.repository.CiudadCaravanaServicioRepository;
import com.ejemplo.caravana.repository.CiudadRepository;
import com.ejemplo.caravana.service.CiudadCaravanaService;
import com.ejemplo.caravana.service.JugadorService;
import com.ejemplo.caravana.service.PermisosService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/ciudades")
public class CiudadController {

    private final CiudadCaravanaService ciudadCaravanaService;
    private final PermisosService permisosService;
    private final JugadorService jugadorService;
    private final CiudadRepository ciudadBaseRepo;
    private final CiudadCaravanaServicioRepository ccsRepo;       

    public CiudadController(CiudadCaravanaService ciudadCaravanaService,
                            PermisosService permisosService,
                            JugadorService jugadorService,
                            CiudadRepository ciudadBaseRepo,
                            CiudadCaravanaServicioRepository ccsRepo) {        
        this.ciudadCaravanaService = ciudadCaravanaService;
        this.permisosService = permisosService;
        this.jugadorService = jugadorService;
        this.ciudadBaseRepo = ciudadBaseRepo;
        this.ccsRepo = ccsRepo;
    }

    /* …─────────── CIUDADES DE LA CARAVANA ──────────── */

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CiudadDTO> listar(HttpSession session) {
        Jugador jugador = jugadorService.obtenerJugadorDesdeSesion(session);
        Caravana caravana = jugador.getCaravana();
        return ciudadCaravanaService.findByCaravana(caravana).stream()
                .map(c -> new CiudadDTO(c.getId(), c.getNombre(), c.getImpuestoEntrada()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CiudadDTO getById(@PathVariable Long id, HttpSession session) {
        Jugador jugador = jugadorService.obtenerJugadorDesdeSesion(session);
        CiudadCaravana c = ciudadCaravanaService.findByIdAndCaravana(id, jugador.getCaravana());
        return new CiudadDTO(c.getId(), c.getNombre(), c.getImpuestoEntrada());
    }

    /* ─── catálogo completo (para el Pre-juego) ─── */
    @GetMapping("/todas")
    public List<CiudadDTO> listarTodas() {
        return ciudadBaseRepo.findAll().stream()
                .map(c -> new CiudadDTO(c.getId(), c.getNombre(), 0.0))
                .collect(Collectors.toList());
    }

    /* ─── asociación de ciudad base existente ─── */
    @PostMapping("/asociar")
    public ResponseEntity<Void> asociarCiudadExistente(@RequestBody AsociarCiudadRequest req,
                                                       HttpSession session) {
        permisosService.validarAdministrador(session);
        Jugador jugador = jugadorService.obtenerJugadorDesdeSesion(session);

        Ciudad ciudadBase = ciudadBaseRepo.findById(req.getCiudadId())
                .orElseThrow(() -> new IllegalArgumentException("Ciudad base no encontrada"));

        CiudadCaravana cc = new CiudadCaravana(
                ciudadBase.getNombre(),
                req.getImpuestos(),
                jugador.getCaravana(),
                ciudadBase);

        ciudadCaravanaService.guardar(cc);
        return ResponseEntity.ok().build();
    }

    /* ─── CRUD CiudadCaravana ─── */

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CiudadDTO> crearCiudad(@RequestBody CiudadDTO dto, HttpSession session) {
        permisosService.validarAdministrador(session);
        Jugador jugador = jugadorService.obtenerJugadorDesdeSesion(session);

        CiudadCaravana nueva = ciudadCaravanaService.crear(dto.getNombre(),
                                                           dto.getImpuestos(),
                                                           jugador.getCaravana());
        return ResponseEntity.ok(new CiudadDTO(nueva.getId(), nueva.getNombre(), nueva.getImpuestoEntrada()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CiudadDTO> actualizarCiudad(@PathVariable Long id,
                                                      @RequestBody CiudadDTO dto,
                                                      HttpSession session) {
        permisosService.validarAdministrador(session);
        Jugador jugador = jugadorService.obtenerJugadorDesdeSesion(session);

        CiudadCaravana actualizada = ciudadCaravanaService.actualizar(
                id, dto.getNombre(), dto.getImpuestos(), jugador.getCaravana());

        return ResponseEntity.ok(new CiudadDTO(actualizada.getId(),
                                               actualizada.getNombre(),
                                               actualizada.getImpuestoEntrada()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCiudad(@PathVariable Long id, HttpSession session) {
        permisosService.validarAdministrador(session);
        Jugador jugador = jugadorService.obtenerJugadorDesdeSesion(session);
        ciudadCaravanaService.eliminar(id, jugador.getCaravana());
        return ResponseEntity.noContent().build();
    }

    /* ────────────────────────────────────────────────────────────────
       NUEVO ENDPOINT: servicios de la ciudad base
       /api/ciudades/{id}/servicios
       ──────────────────────────────────────────────────────────────── */
    @GetMapping("/{id}/servicios")
    public List<ServicioDTO> serviciosDeCiudad(@PathVariable Long id) {

        var lista = ccsRepo.findByCiudadCaravana_Ciudad_Id(id);

        if (lista.isEmpty())
            throw new ResourceNotFoundException("La ciudad no tiene servicios");

        return lista.stream()
                    .map(ServicioDTO::of)
                    .collect(Collectors.toList());
    }
}
