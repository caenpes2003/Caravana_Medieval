import { Component } from '@angular/core';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

@Component({
  standalone: true,
  selector: 'app-acceso-denegado-dialog',
  templateUrl: './acceso-denegado-dialog.component.html',
  styleUrls: ['./acceso-denegado-dialog.component.scss'],
  imports: [MatDialogModule, MatButtonModule]
})
export class AccesoDenegadoDialogComponent {}
