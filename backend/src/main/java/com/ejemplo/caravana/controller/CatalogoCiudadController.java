package com.ejemplo.caravana.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ejemplo.caravana.dto.CatalogoCiudadDTO;
import com.ejemplo.caravana.dto.CiudadDTO;
import com.ejemplo.caravana.exception.ResourceNotFoundException;
import com.ejemplo.caravana.model.Ciudad;
import com.ejemplo.caravana.repository.CiudadCaravanaRepository;
import com.ejemplo.caravana.repository.CiudadRepository;
import com.ejemplo.caravana.repository.CiudadCaravanaServicioRepository;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/catalogo/ciudades")
public class CatalogoCiudadController {

    private final CiudadRepository repo;
    private final CiudadCaravanaRepository ciudadCaravanaRepo;
    private final CiudadCaravanaServicioRepository ccsRepo;

    public CatalogoCiudadController(CiudadRepository repo,
                                    CiudadCaravanaRepository ciudadCaravanaRepo,
                                    CiudadCaravanaServicioRepository ccsRepo) { 
        this.repo = repo;
        this.ciudadCaravanaRepo = ciudadCaravanaRepo;
        this.ccsRepo = ccsRepo;
    }

    @GetMapping            
    public List<CiudadDTO> todas() {
        return repo.findAll()
                   .stream()
                   .map(c -> new CiudadDTO(c.getId(), c.getNombre(), c.getImpuestoEntrada()))
                   .toList();
    }
   
    @DeleteMapping("/{id}")
    @Transactional
    public void eliminar(@PathVariable Long id) {

        if (ciudadCaravanaRepo.existsCiudadVinculadaEnPartida(id))
            throw new IllegalStateException("No se puede eliminar: ciudad en uso");
         
        ccsRepo.deleteByCiudadBase(id);

        /*     Eliminamos primero los clones   */
        ciudadCaravanaRepo.deleteByCiudad_Id(id);

        /*   Luego la ciudad base            */
        repo.deleteById(id);
    }

    @PostMapping
    public ResponseEntity<CiudadDTO> crear(@RequestBody CatalogoCiudadDTO dto){

        repo.findByNombreIgnoreCase(dto.nombre())
            .ifPresent(c -> { throw new IllegalArgumentException("La ciudad ya existe"); });

        Ciudad c = repo.save(new Ciudad(dto.nombre(), dto.impuesto()));
        CiudadDTO body = new CiudadDTO(c.getId(), c.getNombre(), c.getImpuestoEntrada());
        return ResponseEntity.ok(body);
    }
        
        @PutMapping("/{id}") public CiudadDTO actualizar(@PathVariable Long id,@RequestBody CatalogoCiudadDTO dto){ 
            Ciudad c = repo.findById(id) 
            .orElseThrow(() -> new ResourceNotFoundException("No existe")); 
            c.setNombre(dto.nombre()); c.setImpuestoEntrada(dto.impuesto()); 
            repo.save(c); 
            return new CiudadDTO(c.getId(),c.getNombre(),c.getImpuestoEntrada()); 
        }
       

        
}

