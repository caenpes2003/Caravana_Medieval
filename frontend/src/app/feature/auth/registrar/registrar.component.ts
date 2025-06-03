import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

import {
  CommonModule,
  NgIf,
  NgFor,
} from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-registrar',
  standalone: true,
  encapsulation: ViewEncapsulation.None,
  templateUrl: './registrar.component.html',
  styleUrls: ['./registrar.component.scss'],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    NgIf,
    NgFor,
  ],
})
export class RegistrarComponent implements OnInit {
  form!: FormGroup;
  cargando = false;
  caravanas: { id: number; nombre: string }[] = [];

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router,
    private snack: MatSnackBar
  ) {}

  /* ───────────────────── lifecycle ───────────────────── */
  ngOnInit(): void {
    this.form = this.fb.group({
      nombre: ['', Validators.required],
      password: ['', Validators.required],
      rol: ['CARAVANERO', Validators.required],
      nombreCaravana: [''],
      caravanaId: [null],
    });

    /* Ajusta validadores al cambiar el rol */
    this.form.get('rol')!.valueChanges.subscribe(() => this.ajustarValidadores());
    this.ajustarValidadores();

    /* Cargar caravanas disponibles */
    this.auth.obtenerCaravanasDisponibles().subscribe({
      next: (c) => (this.caravanas = c),
      error: () => (this.caravanas = []),
    });
  }

  /* ───────────────────── helpers ───────────────────── */
  private ajustarValidadores(): void {
    const rol = this.form.get('rol')!.value;
    const nombreCaravanaCtrl = this.form.get('nombreCaravana')!;
    const caravanaIdCtrl = this.form.get('caravanaId')!;

    if (rol === 'ADMINISTRADOR') {
      nombreCaravanaCtrl.setValidators(Validators.required);
      caravanaIdCtrl.clearValidators();
      caravanaIdCtrl.setValue(null);
    } else {
      caravanaIdCtrl.setValidators(Validators.required);
      nombreCaravanaCtrl.clearValidators();
      nombreCaravanaCtrl.setValue('');
    }

    nombreCaravanaCtrl.updateValueAndValidity();
    caravanaIdCtrl.updateValueAndValidity();
  }

  get rolValue() {
    return this.form.value.rol;
  }

  /* ───────────────────── registro ───────────────────── */
  registrar(): void {
    if (this.form.invalid) {
      this.snack.open('Formulario incompleto', 'Cerrar', { duration: 2500 });
      return;
    }

    const dto = {
      nombre: this.form.value.nombre.trim(),
      password: this.form.value.password,
      rol: this.rolValue,
      ...(this.rolValue === 'ADMINISTRADOR'
        ? { nombreCaravana: this.form.value.nombreCaravana.trim() }
        : { caravanaId: this.form.value.caravanaId }),
    };

    this.cargando = true;

    this.auth.registrar(dto).subscribe({
      next: (res) => {
        this.snack.open('Cuenta creada. ¡Bienvenido!', 'Cerrar', { duration: 2500 });
        // Guarda al usuario en sesión para no perderlo al recargar
  sessionStorage.setItem('usuario', JSON.stringify(res));

  //  Redirección por rol
  if (res.rol === 'ADMINISTRADOR') {
    this.router.navigate(['/pre-juego']);
  } else {
    // Caravanero y Comerciante entran directo a la caravana
    this.router.navigate(['/caravana']);
  }
},
      error: (err) => {
        this.cargando = false;
        this.snack.open(err.error || 'Error al registrarse', 'Cerrar', { duration: 3000 });
      },
    });
  }
}
