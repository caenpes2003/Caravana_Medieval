package com.ejemplo.caravana.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ejemplo.caravana.dto.JuegoIniciarRequest;
import com.ejemplo.caravana.dto.JuegoIniciarResponse;
import com.ejemplo.caravana.exception.BusinessException;
import com.ejemplo.caravana.exception.ResourceNotFoundException;
import com.ejemplo.caravana.model.Caravana;
import com.ejemplo.caravana.model.CiudadCaravana;
import com.ejemplo.caravana.repository.CaravanaRepository;
import com.ejemplo.caravana.repository.CiudadCaravanaRepository;

import jakarta.servlet.http.HttpSession;


@Service
@Transactional
public class JuegoService {

    private final CaravanaRepository caravanaRepo;
    private final CiudadCaravanaRepository ciudadCaravanaRepo;
    private final GeneradorRutaService rutaSrv;

    public JuegoService(CaravanaRepository caravanaRepo, CiudadCaravanaRepository ciudadCaravanaRepo, GeneradorRutaService rutaSrv) {
        this.caravanaRepo = caravanaRepo;
        this.ciudadCaravanaRepo = ciudadCaravanaRepo;
        this.rutaSrv = rutaSrv;
    }

    // JuegoService.java
public JuegoIniciarResponse iniciarPartida(HttpSession session,
                                           JuegoIniciarRequest req) {

    System.out.println("🟣 [Service] → req = " + req);

    /* 1️⃣  Validación básica */
    List<Long> ciudadBaseIds = req.getCiudadIds();              // <-- IDs BASE
    if (ciudadBaseIds == null || ciudadBaseIds.size() < 2) {
        throw new IllegalArgumentException("Se requieren al menos dos ciudades");
    }

    /* 2️⃣  Caravana de la sesión */
    Caravana c = traerCaravanaDesdeSesion(session);
    System.out.println("🟣 Caravana en sesión = " + c.getId());

    /* 3️⃣  Convertimos IDs base → clones de ESTA caravana               *
     *     (un mismo ID base puede tener N clones, pero                   *
     *      en tu semilla cada base se clonó 1-a-1, así que tomamos uno)  */
    List<CiudadCaravana> clones = ciudadCaravanaRepo.findByCaravana(c)
        .stream()
        .filter(cc -> ciudadBaseIds.contains(cc.getCiudad().getId()))
        .toList();

    System.out.println("🟣 Clones encontrados = " + clones.size());

    if (clones.size() != ciudadBaseIds.size()) {
        throw new BusinessException("Alguna ciudad no pertenece a la caravana");
    }

    /* 4️⃣  Estado y parámetros de partida */
    c.setEstado("EN_CURSO");
    c.setTiempoLimite(req.getTiempoLimite());
    c.setTiempoTranscurrido(0L);
    c.setUltimaActualizacion(LocalDateTime.now());
    c.setDineroInicial(req.getOroInicial());
    c.setDinero(req.getOroInicial());
    c.setMetaGanancia(req.getMetaMonedas());
    c.setVida(req.getVidaInicial());

    /* atributos base */
    c.setCapacidadOriginal(100.0);
    c.setCapacidadMax(100.0);
    c.setCargaActual(0.0);
    c.setTieneGuardias(false);
    c.setHaViajado(false);
    c.setVelocidad(10.0);

    /* 5️⃣  Itinerario (IDs base) */
    c.setItinerario(ciudadBaseIds);

    /* 6️⃣  Ciudad actual = clon aleatorio */
    CiudadCaravana inicio = clones.get(new Random().nextInt(clones.size()));
    c.setCiudadActualId(inicio.getId());

    /* 7️⃣  Guardar + rutas dinámicas */
    caravanaRepo.save(c);
    rutaSrv.generarRutas(clones);

    /* 8️⃣  Log final */
    System.out.println("✅ Partida iniciada; ciudad inicial = " + inicio.getNombre());

    /* 9️⃣  Respuesta */
    return new JuegoIniciarResponse(
            c.getId(),
            ciudadBaseIds,
            inicio.getNombre(),
            req.getTiempoLimite(),
            req.getOroInicial(),
            req.getMetaMonedas(),
            req.getVidaInicial()
    );
}



    // -------------------------------------------------------------------
    private Caravana traerCaravanaDesdeSesion(HttpSession session) {
        Long id = (Long) session.getAttribute("caravanaId");
        if (id == null) throw new BusinessException("Caravana no asociada a la sesión");
        return caravanaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Caravana no encontrada"));
    }
}
