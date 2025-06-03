import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { CiudadService, Ciudad } from '../../../core/services/ciudad.service';
import { StockService, Stock } from '../../../core/services/stock.service';
import { StockFormularioComponent } from './stock-formulario.component';
import { FormsModule } from '@angular/forms'; 
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-stock',
  standalone: true,
  imports: [
    CommonModule,
    MatSelectModule,
    MatTableModule,
    MatButtonModule,
    FormsModule
  ],
  templateUrl: './admin-stock.component.html',
  styleUrls: ['./admin-stock.component.scss']
})
export class AdminStockComponent implements OnInit {
  ciudades: Ciudad[] = [];
  ciudadIdSeleccionada: number | null = null;
  stock: Stock[] = [];
  columnas: string[] = ['productoNombre', 'cantidad', 'fd', 'fo', 'acciones'];

  constructor(
    private ciudadSvc: CiudadService,
    private stockSvc: StockService,
    private dialog: MatDialog,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.ciudadSvc.getCiudades().subscribe(ciudades => this.ciudades = ciudades);
  }

  cargarStock(): void {
  if (!this.ciudadIdSeleccionada) return;
  
  this.stockSvc.getPorCiudad(this.ciudadIdSeleccionada)
      .subscribe(data => this.stock = data);
}

  abrirDialogCrear(): void {
    const ref = this.dialog.open(StockFormularioComponent, {
      data: { ciudadId: this.ciudadIdSeleccionada }
    });
    ref.afterClosed().subscribe(() => this.cargarStock());
  }

  abrirDialogEditar(s: Stock): void {
    const ref = this.dialog.open(StockFormularioComponent, {
      data: { ...s, ciudadId: this.ciudadIdSeleccionada }
    });
    ref.afterClosed().subscribe(() => this.cargarStock());
  }

  eliminar(id: number): void {
    if (confirm('¿Eliminar este stock?')) {
      this.stockSvc.eliminar(id).subscribe(() => this.cargarStock());
    }
  }
  volver(): void {
   
    this.router.navigate(['/pre-juego']);
  }
}
