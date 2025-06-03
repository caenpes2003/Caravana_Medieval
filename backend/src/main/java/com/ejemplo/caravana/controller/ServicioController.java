package com.ejemplo.caravana.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ejemplo.caravana.dto.ServicioDTO;
import com.ejemplo.caravana.exception.BusinessException;
import com.ejemplo.caravana.service.PermisosService;
import com.ejemplo.caravana.service.ServicioService;
import com.ejemplo.caravana.repository.CiudadCaravanaServicioRepository;
import jakarta.servlet.http.HttpSession;
import com.ejemplo.caravana.repository.ServicioRepository;
import com.ejemplo.caravana.service.ServicioCiudadService;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    private final ServicioService servicioService;
    private final ServicioRepository servicioRepo;
    private final PermisosService permisosService;
    private final CiudadCaravanaServicioRepository ccServicioRepo;
    private final ServicioCiudadService servicioCiudadSrv;

    public ServicioController(ServicioService servicioService,
                              PermisosService permisosService,
                              CiudadCaravanaServicioRepository ccServicioRepo,
                              ServicioRepository servicioRepo,
                              ServicioCiudadService servicioCiudadSrv) {
        this.servicioRepo = servicioRepo;
        this.servicioService = servicioService;
        this.permisosService = permisosService;
        this.ccServicioRepo = ccServicioRepo;
         this.servicioCiudadSrv = servicioCiudadSrv;
    }

    /* ─────────────────────────────────────────────────────────────
       LISTAR  (solo ADMIN)
       ───────────────────────────────────────────────────────────── */
    @GetMapping
    public List<ServicioDTO> listar(HttpSession session) {
        permisosService.validarAdministrador(session);
        Long caravanaId = (Long) session.getAttribute("caravanaId");
        if (caravanaId == null) throw new BusinessException("No hay caravana activa");

        return servicioService.findByCaravanaId(caravanaId).stream()
               .map(s -> new ServicioDTO(s.getId(), s.getTipo(), s.getCosto()))
               .toList();
    }
    @GetMapping("/ciudades/{ciudadId}/servicios")
    @ResponseStatus(HttpStatus.OK)
    public List<ServicioDTO> listarPorCiudad(@PathVariable Long ciudadId) {
        return servicioCiudadSrv.obtenerServiciosDeCiudad(ciudadId);
    }


    /* ─────────────────────────────────────────────────────────────
       CREAR  (solo ADMIN)
       ───────────────────────────────────────────────────────────── */
    @PostMapping
    public ResponseEntity<ServicioDTO> crearServicio(@RequestBody ServicioDTO dto,
                                                     HttpSession session) {
        permisosService.validarAdministrador(session);
        Long caravanaId = (Long) session.getAttribute("caravanaId");
        if (caravanaId == null) throw new BusinessException("No hay caravana activa");

        var nuevo = servicioService.crearParaCaravana(caravanaId, dto.getTipo(), dto.getCosto());
        return ResponseEntity.ok(new ServicioDTO(nuevo.getId(), nuevo.getTipo(), nuevo.getCosto()));
    }

    /* ─────────────────────────────────────────────────────────────
       ACTUALIZAR  (solo ADMIN)
       ───────────────────────────────────────────────────────────── */
    @PutMapping("/{id}")
    public ResponseEntity<ServicioDTO> actualizarServicio(@PathVariable Long id,
                                                          @RequestBody ServicioDTO dto,
                                                          HttpSession session) {
        permisosService.validarAdministrador(session);
        var actualizado = servicioService.actualizar(id, dto.getTipo(), dto.getCosto());
        return ResponseEntity.ok(new ServicioDTO(actualizado.getId(),
                                                 actualizado.getTipo(),
                                                 actualizado.getCosto()));
    }
    /* ─────────────────────────────────────────────────────────────
       ELIMINAR  (solo ADMIN)
       ───────────────────────────────────────────────────────────── */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminar(@PathVariable Long id,
                                         HttpSession session) {

        permisosService.validarAdministrador(session);

        /* 1. Desvincular primero todas las relaciones ciudad-servicio */
        ccServicioRepo.deleteByServicio_Id(id);

        /* 2. Ahora el registro “servicio” ya no tiene FK en uso      */
        servicioRepo.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
