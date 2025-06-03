import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  FormArray,
  Validators,
} from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router, RouterModule } from '@angular/router';
import { MatTooltipModule } from '@angular/material/tooltip';
import { CiudadService, Ciudad } from '../../core/services/ciudad.service';
import { JuegoService } from '../../core/services/juego.service';
import { AuthService } from '../../core/services/auth.service';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatRadioModule } from '@angular/material/radio';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-pre-juego',
  standalone: true,
  imports: [
    CommonModule,
    MatRadioModule,
    ReactiveFormsModule,
    MatCheckboxModule,
    MatButtonModule,
    MatTooltipModule,
    MatSnackBarModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatInputModule,
    MatFormFieldModule,
    RouterModule,
  ],
  templateUrl: './pre-juego.component.html',
  styleUrls: ['./pre-juego.component.scss'],
})
export class PreJuegoComponent implements OnInit {
  ciudades: Ciudad[] = [];
  form!: FormGroup;
  cargando = false;
  rol = '';

  constructor(
    private ciudadSvc: CiudadService,
    private juegoSvc: JuegoService,
    private auth: AuthService,
    private fb: FormBuilder,
    private snack: MatSnackBar,
    private router: Router
  ) {}

  /* ─────────────── lifecycle ─────────────── */
  ngOnInit(): void {
    console.log('✅ PreJuegoComponent inicializado');

    const usuario = this.auth.getUsuario();
    this.rol = usuario?.rol ?? '';
    console.log('🔐 Rol detectado:', this.rol);

    /* ⛔ solo admin */
    if (this.rol !== 'ADMINISTRADOR') {
      this.router.navigate(['/']);
      return;
    }

    /* 🔹 traemos el catálogo completo de ciudades base */
    this.ciudadSvc.listarCiudadesCatalogo().subscribe({
      next: (ciudades) => {
        console.log('📍 Catálogo:', ciudades);
        if (!ciudades.length) {
          this.snack.open('No hay ciudades en el catálogo', 'Cerrar', {
            duration: 3000,
          });
          return;
        }

        this.ciudades = ciudades;
        this.form = this.fb.group({
          /* un checkbox por ciudad */
          seleccion: this.fb.array(
            this.ciudades.map(() => this.fb.control(false))
          ),
          tiempoLimite: [null, Validators.required],
          oroInicial: [null, [Validators.required, Validators.min(1)]],
          metaMonedas: [null, Validators.required],
        });
      },
      error: (e) => console.error('❌ Error cargando catálogo', e),
    });
  }

  /* ───────────── getters de comodidad ───────────── */
  get seleccion(): FormArray {
    return this.form.get('seleccion') as FormArray;
  }

  get botonDeshabilitado(): boolean {
    if (!this.form) return true;
    const seleccionadas =
      this.seleccion.controls.filter((c) => c.value).length;
    const { tiempoLimite, oroInicial, metaMonedas } = this.form.value;
    return !(
      seleccionadas >= 2 &&
      tiempoLimite &&
      oroInicial > 0 &&
      metaMonedas &&
      metaMonedas > oroInicial
    );
  }

  get glowActivo(): boolean {
    return !this.botonDeshabilitado && !this.cargando;
  }

  /* ───────────── acción principal ───────────── */
  onIniciar(): void {
    if (this.form.invalid) return;

    const idsBase: number[] = this.seleccion.controls
      .map((ctrl, i) => (ctrl.value ? this.ciudades[i].id : null))
      .filter((v) => v !== null) as number[];

    if (idsBase.length < 2) {
      this.snack.open('Selecciona al menos 2 ciudades', 'Cerrar', {
        duration: 3000,
      });
      return;
    }

    const { tiempoLimite, oroInicial, metaMonedas } = this.form.value;
    if (metaMonedas <= oroInicial) {
      this.snack.open('La meta debe ser mayor al oro inicial', 'Cerrar', {
        duration: 3000,
      });
      return;
    }

    /* guardamos la selección en localStorage por si el front la necesita luego */
    localStorage.setItem('ciudadesSeleccionadas', JSON.stringify(idsBase));

    const payload = {
      ciudadIds: idsBase, // IDs **base** – el backend buscará los clones
      tiempoLimite,
      oroInicial,
      metaMonedas,
      vidaInicial: 1000,
    };

    console.log('🚀 Payload que se enviará:', payload);

    this.cargando = true;
    this.juegoSvc.iniciar(payload).subscribe({
      next: () => {
        this.cargando = false;
        this.router.navigate(['/caravana']);
      },
      error: (e) => {
        this.cargando = false;
        console.error('❌ Error al iniciar partida', e);
        this.snack.open(
          typeof e.error === 'string' ? e.error : 'Error al iniciar partida',
          'Cerrar',
          { duration: 4000 }
        );
      },
    });
  }
}
