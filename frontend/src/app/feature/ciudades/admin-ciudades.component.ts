// feature/ciudades/admin-ciudades.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule }          from '@angular/common';
import { MatButtonModule }       from '@angular/material/button';
import { MatTableModule }        from '@angular/material/table';
import { MatDialog }             from '@angular/material/dialog';
import { Router }                from '@angular/router';

import { CiudadFormularioComponent } from './ciudad-formulario.component';
import { CiudadService, Ciudad }     from '../../core/services/ciudad.service';

@Component({
  selector   : 'app-admin-ciudades',
  standalone : true,
  imports    : [CommonModule, MatTableModule, MatButtonModule],
  templateUrl: './admin-ciudades.component.html',
  styleUrls  : ['./admin-ciudades.component.scss'],
})
export class AdminCiudadesComponent implements OnInit {

  displayedColumns: string[] = ['nombre', 'impuestos', 'acciones'];
  ciudades: Ciudad[] = [];

  constructor(
    private ciudadService: CiudadService,
    private dialog       : MatDialog,
    private router       : Router
  ) {}

  /* ─────────────────────────────────── */
  ngOnInit(): void { this.cargarCiudades(); }

  private cargarCiudades(): void {
    this.ciudadService
        .listarCiudadesCatalogo()
        .subscribe(cs => (this.ciudades = cs));
  }

  /* ───── Diálogos ───── */

  abrirDialogCrear(): void {
    this.dialog.open(CiudadFormularioComponent, {
        panelClass: 'dialog-ciudad',
        data: { catalogo: true }        // sólo el flag
    })
    .afterClosed()
    .subscribe(ok => ok && this.cargarCiudades());
  }

  abrirDialogEditar(ciudad: Ciudad): void {
    this.dialog.open(CiudadFormularioComponent, {
        panelClass: 'dialog-ciudad',
        data: { ...ciudad, catalogo: true }   // ciudad + flag
    })
    .afterClosed()
    .subscribe(ok => ok && this.cargarCiudades());
  }

  /* ───── Eliminar ───── */
  eliminarCiudad(id: number): void {
    if (!confirm('¿Estás seguro de eliminar esta ciudad?')) { return; }

    this.ciudadService
        .eliminarCiudadCatalogo(id)
        .subscribe(() => this.cargarCiudades());
  }

  volver(): void { this.router.navigate(['/pre-juego']); }
}
