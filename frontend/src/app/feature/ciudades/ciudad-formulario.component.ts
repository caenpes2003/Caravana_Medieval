// feature/ciudades/ciudad-formulario.component.ts
import { Component, Inject, ViewEncapsulation } from '@angular/core';
import { CommonModule }          from '@angular/common';
import { FormBuilder, FormGroup,
         Validators, ReactiveFormsModule }      from '@angular/forms';
import { MatFormFieldModule }   from '@angular/material/form-field';
import { MatInputModule }       from '@angular/material/input';
import { MatButtonModule }      from '@angular/material/button';
import { MatDialogRef,
         MAT_DIALOG_DATA }      from '@angular/material/dialog';
import { Router }               from '@angular/router';

import { CiudadService, Ciudad } from '../../core/services/ciudad.service';

@Component({
  selector     : 'app-ciudad-formulario',
  standalone   : true,
  imports      : [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
  ],
  templateUrl  : './ciudad-formulario.component.html',
  styleUrls    : ['./ciudad-formulario.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class CiudadFormularioComponent {

  form: FormGroup;
  modoEdicion = false;   // ¿vienes con id?
  esCatalogo  = false;   // ¿flag activado?

  constructor(
    private fb           : FormBuilder,
    private ciudadService: CiudadService,
    private dialogRef    : MatDialogRef<CiudadFormularioComponent>,
    private router       : Router,

    /*  ⬇⬇ Aquí la unión de tipos (opción B) ⬇⬇ */
    @Inject(MAT_DIALOG_DATA)
    public data: (Ciudad & { catalogo?: boolean }) | null
  ) {
    this.esCatalogo  = !!this.data?.catalogo;
    this.modoEdicion = !!this.data?.id;

    this.form = this.fb.group({
      nombre   : [this.data?.nombre     ?? '', Validators.required],
      impuestos: [this.data?.impuestos  ?? 0,
                  [Validators.required, Validators.min(0)]],
    });
  }

  /* ───── Guardar ───── */
  guardar(): void {
    if (this.form.invalid) { return; }

    const dto = this.form.value;  

    // a) EDITAR
    if (this.modoEdicion && this.data) {
      this.ciudadService
          .actualizarCiudadCatalogo(this.data.id!, {
             nombre  : dto.nombre,
             impuesto: dto.impuestos
          })
          .subscribe(() => this.dialogRef.close(true));
      return;
    }

    // b) CREAR
    this.ciudadService
        .crearCiudadCatalogo({
          nombre  : dto.nombre,
          impuesto: dto.impuestos
        })
        .subscribe(() => this.dialogRef.close(true));
  }

  cancelar(): void { this.dialogRef.close(false); }
  volver  (): void { this.dialogRef.close(); this.router.navigate(['/pre-juego']); }
}
