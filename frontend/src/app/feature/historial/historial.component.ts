import { Component, OnInit }        from '@angular/core';
import { CommonModule }             from '@angular/common';
import { MatTableModule }           from '@angular/material/table';

import { HistorialService }         from '../../core/services/historial.service';
import { HistorialEntry }           from '../../core/models/historial-entry.model';

@Component({
  selector: 'app-historial',
  standalone: true,
  imports: [ CommonModule, MatTableModule ],
  template: `
    <h2>Historial de la Caravana</h2>
    <table mat-table [dataSource]="datos">
      <ng-container matColumnDef="timestamp">
        <th mat-header-cell *matHeaderCellDef> Fecha </th>
        <td mat-cell *matCellDef="let e"> {{ e.timestamp | date:'short' }} </td>
      </ng-container>
      <ng-container matColumnDef="tipo">
        <th mat-header-cell *matHeaderCellDef> Tipo </th>
        <td mat-cell *matCellDef="let e"> {{ e.tipo }} </td>
      </ng-container>
      <ng-container matColumnDef="detalle">
        <th mat-header-cell *matHeaderCellDef> Detalle </th>
        <td mat-cell *matCellDef="let e"> {{ e.detalle }} </td>
      </ng-container>
      <tr mat-header-row *matHeaderRowDef="['timestamp','tipo','detalle']"></tr>
      <tr mat-row *matRowDef="let row; columns: ['timestamp','tipo','detalle'];"></tr>
    </table>
  `
})
export class HistorialComponent implements OnInit {
  datos: HistorialEntry[] = [];

  constructor(private svc: HistorialService) {}

  ngOnInit() {
    this.svc.getAll().subscribe(list => this.datos = list);
  }
}
