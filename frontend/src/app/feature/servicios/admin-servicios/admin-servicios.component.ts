import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar } from '@angular/material/snack-bar';

import {
  ServicioService,
  Servicio,
} from '../../../core/services/servicio.service';
import { ServicioFormularioComponent } from '../servicio-formulario/servicio-formulario.component';
import { AlertaDialogComponent } from '../../../dialogs/alerta-dialog.component';

@Component({
  selector: 'app-admin-servicios',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatButtonModule, MatIconModule],
  templateUrl: './admin-servicios.component.html',
  styleUrls: ['./admin-servicios.component.scss'],
})
export class AdminServiciosComponent implements OnInit {
  servicios: Servicio[] = [];
  columnas: string[] = ['tipo', 'costo', 'acciones'];
  cargando = false;

  constructor(
    private servicioSvc: ServicioService,
    private dialog: MatDialog,
    private snack: MatSnackBar,
    private router: Router
  ) {}

  /* ─────────────── INIT ─────────────── */
  ngOnInit(): void {
    this.cargarServicios();
  }

  /* ─────────────── DATA ─────────────── */
  cargarServicios(): void {
    this.cargando = true;
    this.servicioSvc.getAll().subscribe({
      next: (data) => (this.servicios = data),
      error: (err) => {
        const msg =
          err.status === 403
            ? 'No hay caravana activa. Inicia sesión de nuevo.'
            : err.error?.message || 'Error al cargar servicios';
        this.snack.open(msg, 'Cerrar', { duration: 3000 });
      },
      complete: () => (this.cargando = false),
    });
  }

  /* ─────────────── CREAR ─────────────── */
  abrirDialogCrear(): void {
    const ref = this.dialog.open(ServicioFormularioComponent, {
      panelClass: 'dialog-servicio',
      data: null,
    });

    ref.afterClosed().subscribe((nuevo?: Servicio) => {
      if (nuevo) this.servicios = [...this.servicios, nuevo];
    });
  }

  /* ─────────────── EDITAR ─────────────── */
  abrirDialogEditar(servicio: Servicio): void {
    const ref = this.dialog.open(ServicioFormularioComponent, {
      panelClass: 'dialog-servicio',
      data: servicio,
    });

    ref.afterClosed().subscribe((actualizado: Servicio | undefined) => {
      if (actualizado) {
        this.servicios = this.servicios.map((s) =>
          s.id === actualizado.id ? actualizado : s
        );
      }
    });
  }

  /* ─────────────── ELIMINAR ─────────────── */
  eliminar(id: number): void {
    const dialogRef = this.dialog.open(AlertaDialogComponent, {
      data: {
        titulo: 'Eliminar Servicio',
        mensaje: '¿Deseas eliminar este servicio permanentemente?',
      },
    });

    dialogRef.afterClosed().subscribe((confirmado: boolean) => {
      if (!confirmado) return;

      this.cargando = true;
      this.servicioSvc.eliminar(id).subscribe({
        next: () =>
          (this.servicios = this.servicios.filter((s) => s.id !== id)),
        error: (err) =>
          this.snack.open(
            err.error?.message || 'Error al eliminar servicio',
            'Cerrar',
            { duration: 3000 }
          ),
        complete: () => (this.cargando = false),
      });
    });
  }

  /* ─────────────── NAVEGACIÓN ─────────────── */
  volver(): void {
    this.router.navigate(['/pre-juego']);
  }
}
