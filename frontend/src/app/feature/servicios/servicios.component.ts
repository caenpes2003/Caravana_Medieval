// src/app/feature/servicios/servicios.component.ts
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule }            from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { MatListModule }           from '@angular/material/list';
import { MatButtonModule }         from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { ServicioService } from '../../core/services/servicio.service';
import { CaravanaService } from '../../core/services/caravana.service';
import { Servicio }        from '../../core/models/servicio.model';

@Component({
  selector   : 'app-servicios',
  standalone : true,
  imports    : [
    CommonModule,
    RouterModule,
    MatListModule,
    MatButtonModule,
    MatSnackBarModule
  ],
  templateUrl: './servicios.component.html',
  styleUrls  : ['./servicios.component.scss']
})
export class ServiciosComponent implements OnInit {

  /* ─── stats & flags ─────────────────────────────── */
  ciudadActualId = 0;
  tieneGuardias  = false;
  serviciosUsados: string[] = [];

  dinero = 0;
  vida   = 0;
  capacidadActual = 0;
  capacidadMax   = 0;

  /* Lista que se dibuja en la vista */
  servicios: (Servicio & { noDisponible: boolean })[] = [];

  /* Animación + feedback */
  mensajeEfecto = '';
  animando      = false;

  constructor(
    private route       : ActivatedRoute,
    private caravanaSvc : CaravanaService,
    private servicioSvc : ServicioService,
    private snack       : MatSnackBar,
    private cdr         : ChangeDetectorRef
  ) {}

  /* ─────────────────────────────────────────────── */
  ngOnInit(): void {
    /* ① Intenta leer /servicios/:id de la URL */
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam && !isNaN(+idParam)) {
      this.ciudadActualId = +idParam;
      this.inicializarServicios();
      return;
    }

    /* ② Caso normal: pregunta al backend dónde está la caravana */
    this.caravanaSvc.getEstado().subscribe({
      next : (c) => {
        if (!c) {
          this.snack.open('Aún no has iniciado partida.', 'Cerrar', { duration: 3000 });
          return;
        }
        this.ciudadActualId = c.ciudadActualId;   // ← FIX aquí
        this.inicializarServicios();
      },
      error: () => this.snack.open('No se pudo obtener estado de la caravana', 'Cerrar', { duration: 3000 })
    });
  }

  /* ─────────────────────────────────────────────── */
  private inicializarServicios(): void {
    /* Estado de la caravana (stats y flags) */
    this.caravanaSvc.getEstado().subscribe({
      next : (c) => {
        if (!c) return;

        this.tieneGuardias   = c.tieneGuardias;
        this.serviciosUsados = c.serviciosUsados;

        this.dinero          = c.dinero;
        this.vida            = c.vida;
        this.capacidadActual = c.cargaActual;
        this.capacidadMax    = c.capacidadMax;

        /* Catálogo específico de la ciudad */
        this.servicioSvc.getPorCiudad(this.ciudadActualId).subscribe({
          next : (lista) => {
            this.servicios = lista.map(svc => ({
              ...svc,
              noDisponible:
                this.serviciosUsados.includes(this.ciudadActualId + '-' + svc.tipo.toUpperCase()) ||
                (svc.tipo.toUpperCase() === 'GUARDIAS' && this.tieneGuardias)
            }));
            this.cdr.detectChanges();
          },
          error: () =>
            this.snack.open('No se pudieron cargar los servicios', 'Cerrar', { duration: 3000 })
        });
      },
      error: () =>
        this.snack.open('No se pudo cargar estado de la caravana', 'Cerrar', { duration: 3000 })
    });
  }

  /* ─────────────────────────────────────────────── */
  usarServicio(servicioId: number): void {
    if (!this.ciudadActualId) return;

    this.caravanaSvc.usarServicio(servicioId, this.ciudadActualId).subscribe({
      next : (updated) => {
        this.snack.open('✅ Servicio usado con éxito', 'Cerrar', { duration: 3000 });

        // feedback visual
        if (updated.vida > this.vida)          this.mostrarEfecto('+50 ❤️');
        if (updated.capacidadMax > this.capacidadMax) this.mostrarEfecto('📦 Capacidad ↑');

        // refrescar stats
        this.dinero          = updated.dinero;
        this.vida            = updated.vida;
        this.capacidadActual = updated.cargaActual;
        this.capacidadMax    = updated.capacidadMax;
        this.tieneGuardias   = updated.tieneGuardias;
        this.serviciosUsados = updated.serviciosUsados;

        this.inicializarServicios();           // recargar lista
      },
      error: (err) => {
        const msg = err?.error?.message || err?.error || '❌ Error al usar servicio';
        this.snack.open(msg, 'Cerrar', { duration: 3000 });
      }
    });
  }

  /* Pequeña animación de texto flotante */
  mostrarEfecto(texto: string): void {
    this.mensajeEfecto = texto;
    this.animando      = true;
    setTimeout(() => (this.animando = false), 1500);
  }
}
