import { Component, Inject, ViewEncapsulation } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ServicioService, Servicio } from '../../../core/services/servicio.service';

@Component({
  selector   : 'app-servicio-formulario',
  standalone : true,
  imports    : [CommonModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './servicio-formulario.component.html',
  styleUrls  : ['./servicio-formulario.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class ServicioFormularioComponent {

  form       : FormGroup;
  modoEdicion: boolean;

  constructor(
    private fb          : FormBuilder,
    private servicioSvc : ServicioService,
    private dialogRef   : MatDialogRef<ServicioFormularioComponent>,
    private snack       : MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: Servicio | null
  ) {
    this.modoEdicion = !!data;
    this.form = this.fb.group({
      tipo : [data?.tipo  || '', [Validators.required]],
      costo: [data?.costo || 0 , [Validators.required, Validators.min(0)]]
    });
  }

  guardar(): void {
  if (this.form.invalid) return;

  const dto = { tipo: this.form.value.tipo, costo: this.form.value.costo };

  const req$ = this.modoEdicion
      ? this.servicioSvc.actualizar(this.data!.id, dto)
      : this.servicioSvc.crear(dto);

  req$.subscribe({
    next: servicio => this.dialogRef.close(servicio),   // devuelve el objeto
    error: err  => this.snack.open(err.error || 'Error', 'Cerrar',
                                   { duration: 2500 })
  });
}


  cancelar(): void { this.dialogRef.close(false); }
}
