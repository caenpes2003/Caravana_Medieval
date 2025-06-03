package com.ejemplo.caravana.service;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ejemplo.caravana.dto.JuegoIniciarRequest;
import com.ejemplo.caravana.dto.StockDTO;
import com.ejemplo.caravana.exception.BusinessException;
import com.ejemplo.caravana.exception.ResourceNotFoundException;
import com.ejemplo.caravana.exception.VictoryException;
import com.ejemplo.caravana.model.Caravana;
import com.ejemplo.caravana.model.Ciudad;
import com.ejemplo.caravana.model.CiudadCaravana;
import com.ejemplo.caravana.model.Historial;
import com.ejemplo.caravana.model.InventarioCaravana;
import com.ejemplo.caravana.model.Ruta;
import com.ejemplo.caravana.model.ServicioEntity;
import com.ejemplo.caravana.model.Stock;
import com.ejemplo.caravana.repository.CaravanaRepository;
import com.ejemplo.caravana.repository.CiudadCaravanaRepository;
import com.ejemplo.caravana.repository.CiudadRepository;
import com.ejemplo.caravana.repository.InventarioCaravanaRepository;
import com.ejemplo.caravana.repository.RutaRepository;
import com.ejemplo.caravana.repository.ServicioRepository;
import com.ejemplo.caravana.repository.StockRepository;

import jakarta.servlet.http.HttpSession;

@Service
@Transactional
public class CaravanaService {

    private static final Logger log = LoggerFactory.getLogger(CaravanaService.class);
    private final DataSource dataSource;
    private final CaravanaRepository carRepo;
    private final RutaRepository rutaRepo;
    private final StockRepository stockRepo;
    private final ServicioRepository servicioRepo;
    private final HistorialService historialService;
    private final InventarioCaravanaRepository inventarioRepo;

    @Autowired
    private final CiudadRepository ciudadBaseRepo;
    private final CiudadCaravanaRepository ciudadCaravanaRepo;
    private final ServicioService servicioService;

    public CaravanaService(
            CaravanaRepository carRepo,
            RutaRepository rutaRepo,
            StockRepository stockRepo,
            ServicioRepository servicioRepo,
            HistorialService historialService,
            DataSource dataSource,
            InventarioCaravanaRepository inventarioRepo,
            CiudadCaravanaRepository ciudadCaravanaRepo,
            CiudadRepository ciudadBaseRepo,
            ServicioService servicioService

    ) {
        this.carRepo = carRepo;
        this.rutaRepo = rutaRepo;
        this.stockRepo = stockRepo;
        this.servicioRepo = servicioRepo;
        this.historialService = historialService;
        this.dataSource = dataSource;
        this.inventarioRepo = inventarioRepo;
        this.ciudadCaravanaRepo = ciudadCaravanaRepo;
        this.ciudadBaseRepo = ciudadBaseRepo;
        this.servicioService = servicioService;

    }

    public Caravana iniciarPartida(JuegoIniciarRequest req) {
        Caravana c = getCaravanaActual();

        c.setEstado("EN_CURSO");

        // 1. Cargo los clones de ciudad de ESTA caravana
        List<CiudadCaravana> clones = ciudadCaravanaRepo.findAllById(req.getCiudadIds());

        // 2. Validar que todas pertenecen a la caravana actual
        for (CiudadCaravana clone : clones) {
            if (!clone.getCaravana().getId().equals(c.getId())) {
                throw new BusinessException("Ciudad no pertenece a esta caravana");
            }
        }

        // 3. Guardar el itinerario con IDs de ciudad base
        List<Long> ciudadBaseIds = clones.stream()
                .map(cc -> cc.getCiudad().getId())
                .toList();
        c.setItinerario(ciudadBaseIds);

        // 4. Guardar el ID real (de la tabla ciudad_caravana) como ciudad actual
        if (!clones.isEmpty()) {
            c.setCiudadActualId(clones.get(0).getId()); // ✅ Este ID es de ciudad_caravana
        }

        // Resto de atributos
        c.setTiempoLimite(req.getTiempoLimite());
        c.setTiempoTranscurrido(0L);
        c.setUltimaActualizacion(LocalDateTime.now());
        c.setDineroInicial(req.getOroInicial());
        c.setDinero(req.getOroInicial());
        c.setMetaGanancia(req.getMetaMonedas());
        c.setVida(req.getVidaInicial());
        c.setCapacidadOriginal(100.0);
        c.setCapacidadMax(100.0);
        c.setCargaActual(0.0);
        c.setTieneGuardias(false);
        c.setHaViajado(false);
        c.setVelocidad(0.0);

        return carRepo.save(c);
    }

    public Caravana guardar(Caravana caravana) {
        return carRepo.save(caravana);
    }

    public Caravana getEstado() {
        Caravana c = getCaravanaActual();

        if ("FINALIZADA".equals(c.getEstado())) {
            return c; // ya finalizada, no actualizamos más
        }

        LocalDateTime ahora = LocalDateTime.now();

        if (c.getUltimaActualizacion() != null) {
            long segundos = java.time.Duration.between(c.getUltimaActualizacion(), ahora).getSeconds();
            c.setTiempoTranscurrido(c.getTiempoTranscurrido() + segundos);
        }

        c.setUltimaActualizacion(ahora);

        if (c.getTiempoTranscurrido() >= c.getTiempoLimite()) {
            c.setTiempoTranscurrido(c.getTiempoLimite());
        }

        carRepo.save(c);
        checkGameOverOrWin(c);

        return c;
    }

    public Caravana pagarImpuestos(Long rutaId) {
        Caravana c = getEstado();
        Ruta r = rutaRepo.findById(rutaId)
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada"));

        double impuesto = r.getCiudadDestino().getImpuestoEntrada();
        if (c.getDinero() < impuesto) {
            throw new BusinessException("Dinero insuficiente para impuestos de " + r.getCiudadDestino().getNombre());
        }

        c.setDinero(c.getDinero() - impuesto);

        historialService.registrar(new Historial(
                null,
                LocalDateTime.now(),
                "IMPUESTO",
                "Pago impuesto ciudad " + r.getCiudadDestino().getNombre() + " = " + impuesto,
                c));

        return carRepo.save(c);
    }

    public Caravana viajar(Long rutaId) {
        Caravana c = getEstado();
        Ruta r = rutaRepo.findById(rutaId)
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada"));

        double danio = 0.0;
        if (!r.isSegura() && !c.isTieneGuardias()) {
            danio = r.getDano();
            c.setVida(Math.max(0, c.getVida() - danio));
        }
        if (danio > 0) {
            historialService.registrar(new Historial(
                    null,
                    LocalDateTime.now(),
                    "DAÑO",
                    "Ruta peligrosa: -" + danio + " de vida por " + r.getTipoDanio(),
                    c));
        }

        c.setCiudadActualId(r.getCiudadDestino().getId());
        c.setHaViajado(true);

        c.setVelocidad(r.getVelocidad());

        historialService.registrar(new Historial(
                null,
                LocalDateTime.now(),
                "VIAJE",
                "Ruta " + r.getCiudadOrigen().getNombre() + "→" + r.getCiudadDestino().getNombre() + " daño=" + danio,
                c));

        Caravana updated = carRepo.save(c);
        checkGameOverOrWin(updated);
        return updated;
    }

    public Caravana comprar(Long ciudadId, Long productoId, int cantidad) {
        Caravana c = getEstado();
        Stock stock = stockRepo.findByCiudadIdAndProductoId(ciudadId, productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no disponible en la ciudad"));

        if (cantidad > stock.getCantidad()) {
            throw new BusinessException("Stock insuficiente en la ciudad");
        }

        double precioUnitario = stock.getFO() / (1 + stock.getCantidad());
        double total = precioUnitario * cantidad;

        if (c.getDinero() < total) {
            throw new BusinessException("Dinero insuficiente para comprar");
        }

        stock.setCantidad(stock.getCantidad() - cantidad);
        stockRepo.save(stock);

        c.setDinero(c.getDinero() - total);
        c.setCargaActual(c.getCargaActual() + cantidad);

        // ⬇️ Agregar al inventario de la caravana
        InventarioCaravana inv = inventarioRepo
                .findByCaravanaIdAndProductoId(c.getId(), productoId)
                .orElse(new InventarioCaravana(c, stock.getProducto(), 0));

        inv.setCantidad(inv.getCantidad() + cantidad);
        inventarioRepo.save(inv);

        historialService.registrar(new Historial(
                null,
                LocalDateTime.now(),
                "COMPRA",
                "Producto " + productoId + " ×" + cantidad,
                c));

        Caravana updated = carRepo.save(c);
        checkGameOverOrWin(updated);
        return updated;
    }

    public Caravana vender(Long ciudadId, Long productoId, int cantidad) {
        Caravana c = getEstado();
        Stock stock = stockRepo.findByCiudadIdAndProductoId(ciudadId, productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no disponible en la ciudad"));

        if (cantidad > c.getCargaActual()) {
            throw new BusinessException("No tienes esa cantidad para vender");
        }

        // ⬇️ Verificar y restar del inventario de la caravana
        InventarioCaravana inv = inventarioRepo
                .findByCaravanaIdAndProductoId(c.getId(), productoId)
                .orElseThrow(() -> new BusinessException("No tienes ese producto en tu inventario"));

        if (inv.getCantidad() < cantidad) {
            throw new BusinessException("No tienes suficiente cantidad para vender");
        }

        inv.setCantidad(inv.getCantidad() - cantidad);
        if (inv.getCantidad() == 0) {
            inventarioRepo.delete(inv);
        } else {
            inventarioRepo.save(inv);
        }

        // ⬇️ Calcular ingreso y actualizar stock en ciudad
        double precioUnitario = stock.getFD() / (1 + stock.getCantidad());
        double ingreso = precioUnitario * cantidad;

        stock.setCantidad(stock.getCantidad() + cantidad);
        stockRepo.save(stock);

        c.setDinero(c.getDinero() + ingreso);
        c.setCargaActual(c.getCargaActual() - cantidad);

        historialService.registrar(new Historial(
                null,
                LocalDateTime.now(),
                "VENTA",
                "Producto " + productoId + " vendido ×" + cantidad,
                c));

        Caravana updated = carRepo.save(c);
        checkGameOverOrWin(updated);
        return updated;
    }

    public List<Caravana> findAll() {
        return carRepo.findAll();
    }

    public Caravana findById(Long id) {
        return carRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Caravana no encontrada"));
    }

    public Caravana usarServicio(Long servicioId, Long ciudadId) {
        Caravana c = getEstado();
        ServicioEntity svc = servicioRepo.findById(servicioId)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));

        String tipo = svc.getTipo().toUpperCase();
        String clavePorCiudad = ciudadId + "-" + tipo;

        if (c.getServiciosUsados().contains(clavePorCiudad)) {
            throw new BusinessException("Servicio '" + tipo + "' ya fue usado en esta ciudad.");
        }

        if (tipo.equals("GUARDIAS") && c.getServiciosUsados().contains("GUARDIAS")) {
            throw new BusinessException("Guardias ya fueron contratados en esta partida.");
        }

        if (c.getDinero() < svc.getCosto()) {
            throw new BusinessException("Dinero insuficiente para servicio: " + tipo);
        }

        switch (tipo) {
            case "REPARAR" -> c.setVida(c.getVida() + 50);
            case "MEJORARCAPACIDAD" -> {
                double max = c.getCapacidadOriginal() * 4.0;
                if (c.getCapacidadMax() >= max)
                    throw new BusinessException("Capacidad máxima alcanzada.");
                c.setCapacidadMax(Math.min(max, c.getCapacidadMax() + c.getCapacidadOriginal() * 0.4));
            }
            case "MEJORARVELOCIDAD" -> {
                double base = c.getVelocidad() / 1.2;
                double max = base * 1.5;
                if (c.getVelocidad() >= max)
                    throw new BusinessException("Velocidad máxima alcanzada.");
                c.setVelocidad(Math.min(max, c.getVelocidad() + c.getVelocidad() * 0.2));
            }
            case "GUARDIAS" -> c.setTieneGuardias(true);
            default -> throw new BusinessException("Servicio desconocido: " + tipo);
        }

        c.setDinero(c.getDinero() - svc.getCosto());

        if (tipo.equals("GUARDIAS")) {
            c.getServiciosUsados().add("GUARDIAS");
        } else {
            c.getServiciosUsados().add(clavePorCiudad);
        }

        historialService.registrar(new Historial(
                null,
                LocalDateTime.now(),
                "SERVICIO",
                "Servicio " + tipo + " en ciudad " + ciudadId,
                c));

        Caravana updated = carRepo.save(c);
        checkGameOverOrWin(updated);
        return updated;
    }

    public List<StockDTO> obtenerInventario() {
        Caravana c = getEstado();
        return inventarioRepo.findByCaravanaId(c.getId())
                .stream()
                .map(inv -> new StockDTO(
                        null,
                        inv.getProducto().getId(),
                        inv.getProducto().getNombre(),
                        inv.getCantidad(),
                        0.0,
                        0.0))
                .toList();
    }

    public void reset(HttpSession session) {

        Caravana c = getCaravanaActual();

        historialService.borrarPorCaravana(c.getId());

        log.info("Reiniciando caravana id={}", c.getId());
        c.setNombre("MiCaravana");
        c.setVelocidad(10.0);
        c.setCapacidadOriginal(100.0);
        c.setCapacidadMax(100.0);
        c.setCargaActual(0.0);
        c.setDinero(200.0);
        c.setVida(100.0);
        c.setTiempoLimite(300L);
        c.setMetaGanancia(0.0);
        c.setTieneGuardias(false);
        c.setCiudadActualId(null);
        c.setHaViajado(false);
        c.setUltimaActualizacion(LocalDateTime.now());
        c.setTiempoTranscurrido(0L);
        c.getItinerario().clear();
        c.getServiciosUsados().clear();

        carRepo.save(c);

        // Limpieza completa de stock
        stockRepo.deleteAll();
        stockRepo.flush();
        // Limpieza completa del inventario de la caravana
        inventarioRepo.deleteAll();
        inventarioRepo.flush();

        // Ejecutar el SQL sobre la CONEXIÓN de Spring, y no sobre una nueva
        Connection conn = DataSourceUtils.getConnection(dataSource);
        try {
            ScriptUtils.executeSqlScript(
                    conn,
                    new ClassPathResource("stock-inicial.sql"));
            log.info("✅ Stock reiniciado correctamente");
        } catch (ScriptException e) {
            log.error("❌ Error al ejecutar stock-inicial.sql", e);
            throw new BusinessException("Error al reiniciar el stock de las ciudades");
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }

    public Caravana crearCaravanaPorDefecto(String nombreCaravana) {

        Caravana caravana = new Caravana();
        /* ─── atributos base ─── */
        caravana.setNombre(nombreCaravana);
        caravana.setVelocidad(1.0);
        caravana.setCapacidadOriginal(100.0);
        caravana.setCapacidadMax(100.0);
        caravana.setCargaActual(0.0);
        caravana.setDineroInicial(100.0);
        caravana.setDinero(100.0);
        caravana.setVida(100.0);
        caravana.setTieneGuardias(false);
        caravana.setHaViajado(false);
        caravana.setTiempoLimite(300L);
        caravana.setTiempoTranscurrido(0L);
        caravana.setUltimaActualizacion(LocalDateTime.now());
        caravana.setMetaGanancia(500.0);
        caravana.setCiudadActualId(null);
        caravana.setItinerario(new ArrayList<>());
        caravana.setServiciosUsados(new ArrayList<>());

        try {
            /* 1. Persistir caravana vacía */
            Caravana guardada = carRepo.save(caravana);

            /* 2. Clonar ciudades base → ciudad_caravana */
            List<Ciudad> ciudadesBase = ciudadBaseRepo.findAll();
            List<CiudadCaravana> ciudadesClonadas = ciudadesBase.stream()
                    .map(c -> new CiudadCaravana(c.getNombre(), c.getImpuestoEntrada(), guardada, c))
                    .toList();
            ciudadCaravanaRepo.saveAll(ciudadesClonadas);

            /* 3. Clonar servicios base (tabla servicio_base) para ESTA caravana */
            servicioService.clonarServiciosBaseParaCaravana(guardada);

            /* 4. Relacionar cada ciudad-caravana con TODOS los servicios clonados */
            servicioService.vincularServiciosACiudades(guardada, ciudadesClonadas);

            return guardada;

        } catch (Exception e) {
            System.err.println("❌ Error al guardar la caravana: " + e.getMessage());
            throw new BusinessException("Error al guardar la caravana: " + e.getMessage());
        }
    }
  

    private void checkGameOverOrWin(Caravana c) {

        if (c.getTiempoTranscurrido() >= c.getTiempoLimite()) {
            c.setEstado("FINALIZADA");
            carRepo.save(c);
            throw new BusinessException("Se acabó el tiempo: Game Over");
        }

        if (c.getDinero() <= 0 || c.getVida() <= 0) {
            c.setEstado("FINALIZADA");
            carRepo.save(c);
            throw new BusinessException("Game Over: Dinero o vida agotados");
        }

        if (c.getDinero() >= c.getMetaGanancia()) {
            c.setEstado("FINALIZADA");
            carRepo.save(c);
            throw new VictoryException("¡Meta alcanzada! Victoria");
        }

    }

    private Caravana getCaravanaActual() {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getSession(false);

        if (session == null) {
            throw new BusinessException("Sesión no encontrada");
        }
        Long caravanaId = (Long) session.getAttribute("caravanaId");
        if (caravanaId == null) {
            throw new BusinessException("Caravana no asociada a la sesión");
        }
        return carRepo.findById(caravanaId)
                .orElseThrow(() -> new ResourceNotFoundException("Caravana no encontrada"));
    }

}
