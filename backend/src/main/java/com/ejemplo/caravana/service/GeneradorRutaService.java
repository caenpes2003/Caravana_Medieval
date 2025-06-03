package com.ejemplo.caravana.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ejemplo.caravana.model.CiudadCaravana;
import com.ejemplo.caravana.model.Ruta;
import com.ejemplo.caravana.repository.RutaRepository;

@Service
public class GeneradorRutaService {

    @Autowired
    private RutaRepository rutaRepository;

    
    public void generarRutas(List<CiudadCaravana> ciudadesSeleccionadas) {
        rutaRepository.deleteAll(); // Elimina las rutas previas si hay

        Random rand = new Random();

        for (CiudadCaravana origen : ciudadesSeleccionadas) {
        for (CiudadCaravana destino : ciudadesSeleccionadas) {
            if (!origen.getId().equals(destino.getId())) {
                Ruta ruta = new Ruta();
                ruta.setCiudadOrigen(origen);
                ruta.setCiudadDestino(destino);

                    boolean esSegura = rand.nextBoolean();
                    ruta.setSegura(esSegura);

                    double distancia;
                    int tiempoEnSegundos = 1 + rand.nextInt(10); // tiempo entre 1 y 10 segundos
                    ruta.setTiempo(tiempoEnSegundos);

                    if (!esSegura) {
                        // Ruta insegura: más corta (500 - 999)
                        distancia = 500 + rand.nextInt(500);
                        ruta.setTipoDanio(obtenerTipoDanioAleatorio(rand));
                        ruta.setDano(5 + rand.nextInt(26)); // daño entre 5 y 30
                    } else {
                        // Ruta segura: más larga (1200 - 1999)
                        distancia = 1200 + rand.nextInt(800);
                        ruta.setTipoDanio(null);
                        ruta.setDano(0);
                    }

                    ruta.setDistancia(distancia);

                    // Velocidad = distancia / tiempo
                    double velocidad = distancia / tiempoEnSegundos;
                    ruta.setVelocidad(velocidad); 

                    // Impuesto de entrada a la ciudad de destino
                    ruta.setImpuestoDestino(destino.getImpuestoEntrada());

                    rutaRepository.save(ruta);
                }
            }
        }
    }

    private String obtenerTipoDanioAleatorio(Random rand) {
        String[] tipos = {"Bandidos", "Derrumbe", "Desastre Natural", "Animales Salvajes", "Tormenta"};
        return tipos[rand.nextInt(tipos.length)];
    }
}
