import { Component, Inject, OnInit, ViewEncapsulation } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { finalize } from 'rxjs/operators';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { StockService } from '../../../core/services/stock.service';

import {
  ProductoService,
  Producto,
} from '../../../core/services/producto.service';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-stock-formulario',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
  ],
  templateUrl: './stock-formulario.component.html',
  styleUrls: ['./stock-formulario.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class StockFormularioComponent implements OnInit {
  saving = false;
  form: FormGroup;
  modoEdicion: boolean;
  productos: Producto[] = [];

  constructor(
    private fb: FormBuilder,
    private stockSvc: StockService,
    private productoSvc: ProductoService,
    private dialogRef: MatDialogRef<StockFormularioComponent>,
    private cdr: ChangeDetectorRef,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.modoEdicion = !!data?.id;

    this.form = this.fb.group({
      productoId: [data?.productoId || '', Validators.required],
      cantidad: [data?.cantidad || 0, [Validators.required, Validators.min(0)]],
      fd: [data?.fd || 1.0, [Validators.required, Validators.min(0.1)]],
      fo: [data?.fo || 1.0, [Validators.required, Validators.min(0.1)]],
    });
  }

  ngOnInit(): void {
    this.productoSvc.getAll().subscribe((productos) => {
      this.productos = productos;
    });
  }

  errorDuplicado: boolean = false;

  guardar(): void {
    if (this.form.invalid || this.saving) return;

    this.errorDuplicado = false;
    this.cdr.detectChanges(); // si usas ChangeDetectorRef
    this.saving = true;

    const dto = { ...this.form.value };

    if (this.modoEdicion) {
      this.stockSvc
        .actualizar(this.data.id, dto)
        .pipe(finalize(() => (this.saving = false)))
        .subscribe({
          next: () => this.dialogRef.close(true),
          error: (err) => {
            console.error('❌ Error al actualizar:', err);
          },
        });
    } else {
      this.stockSvc
        .crear(this.data.ciudadId, dto)
        .pipe(finalize(() => (this.saving = false)))
        .subscribe({
          next: () => this.dialogRef.close(true),
          error: (err) => {
            console.error('❌ Error al crear:', err);
            console.log('🧪 err.error:', err.error);
            if (
              typeof err.error === 'string' &&
              err.error.includes('Ya existe stock')
            ) {
              this.errorDuplicado = true;
            }
          },
        });
    }
  }

  cancelar(): void {
    this.dialogRef.close(false);
  }
}
