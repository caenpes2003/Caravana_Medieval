// rutas.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterModule, Router } from '@angular/router';
import { concatMap, delay } from 'rxjs/operators';
import { MatIconModule } from '@angular/material/icon';
import { RutaService } from '../../core/services/ruta.service';
import { CaravanaService } from '../../core/services/caravana.service';
import { Ruta } from '../../core/models/ruta.model';
import { MatCardModule } from '@angular/material/card';
import { MatTooltipModule } from '@angular/material/tooltip';
import { trigger, transition, style, animate } from '@angular/animations';

@Component({
  selector: 'app-rutas',
  standalone: true,
  templateUrl: './rutas.component.html',
  styleUrls: ['./rutas.component.scss'],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule,
    MatSnackBarModule,
    RouterModule,
    MatIconModule,
    MatCardModule,
    MatTooltipModule
  ],
  animations: [
    trigger('slideFade', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(-10px)' }),
        animate('300ms ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
      ]),
      transition(':leave', [
        animate('200ms ease-in', style({ opacity: 0, transform: 'translateY(-10px)' }))
      ])
    ])
  ]
})

export class RutasComponent implements OnInit {
  rutas: Ruta[] = [];
  rutasFiltradas: Ruta[] = [];
  fg: FormGroup;
  rutaSeleccionada?: Ruta;
  animando = false;
  narrativaRuta = '';
  historialRutas: string[] = [];
  logroVeterano = false;
  ciudadesSeleccionadas: number[] = [];

  constructor(
    private rutaSvc: RutaService,
    private caravanSvc: CaravanaService,
    private fb: FormBuilder,
    private snack: MatSnackBar,
    private router: Router
  ) {
    this.fg = this.fb.group({ rutaId: [null, Validators.required] });
  }

  ngOnInit() {
    console.log("📍 Ciudades seleccionadas:", this.ciudadesSeleccionadas);
  
    const ciudadesStorage = localStorage.getItem('ciudadesSeleccionadas');
    if (ciudadesStorage) {
      this.ciudadesSeleccionadas = JSON.parse(ciudadesStorage);
    }
  
    this.rutaSvc.getAll().subscribe(r => {
      console.log("🛣️ Todas las rutas recibidas:", r);
      this.rutas = r;
  
      // Marcar rutas que tienen su reversa (bidireccionales)
      this.rutasFiltradas = r.map(ruta => {
        const reversa = r.some(
          otra =>
            otra.ciudadOrigenNombre === ruta.ciudadDestinoNombre &&
            otra.ciudadDestinoNombre === ruta.ciudadOrigenNombre
        );
        return { ...ruta, reversa };
      });
    });
  
    this.fg.get('rutaId')?.valueChanges.subscribe(id => {
      this.rutaSeleccionada = this.rutasFiltradas.find(r => r.id === id);
      if (this.rutaSeleccionada) {
        this.narrativaRuta = this.generarNarrativa(this.rutaSeleccionada);
      }
    });
  
    this.cargarHistorial();
  }
  
  
  
  generarNarrativa(ruta: Ruta): string {
    let partes: string[] = [];
    if (!ruta.segura) partes.push('Un sendero peligroso donde se rumorea que acechan bandidos.');
    if (ruta.impuestoDestino > 0) partes.push(`La ciudad cobra ${ruta.impuestoDestino} MO al entrar.`);
    if (ruta.distancia > 800) partes.push('Un viaje largo que pondrá a prueba tu caravana.');
    if (partes.length === 0) partes.push('Un camino tranquilo y sin riesgos aparentes.');
    return partes.join(' ');
  }

  onViajar() {
    const id = this.fg.value.rutaId as number;
    this.animando = true;

    this.caravanSvc.pagarImpuestos(id).pipe(
      concatMap(() => this.caravanSvc.viajar(id)),
      delay((this.rutaSeleccionada?.tiempo ?? 2) * 1000)

    ).subscribe({
      next: () => {
        const nuevo = `${this.rutaSeleccionada?.ciudadOrigenNombre} → ${this.rutaSeleccionada?.ciudadDestinoNombre}`;
        const historial = JSON.parse(localStorage.getItem('historialRutas') || '[]');
        historial.push(nuevo);
        localStorage.setItem('historialRutas', JSON.stringify(historial));

        localStorage.removeItem('ciudadesSeleccionadas');
        this.router.navigate(['/caravana']);
      },
      error: e => {
        this.snack.open(e.error || e.message, 'Cerrar', { duration: 3000 });
        this.animando = false;
      }
    });
  }

  cargarHistorial() {
    const data = localStorage.getItem('historialRutas');
  this.historialRutas = data ? JSON.parse(data) : [];
  localStorage.setItem('historialRutas', JSON.stringify(this.historialRutas)); 

    this.logroVeterano = this.historialRutas.length >= 3;
  }
  
}
