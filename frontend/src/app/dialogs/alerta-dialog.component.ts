import { Component, Inject, ViewEncapsulation } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-alerta-dialog',
  imports: [CommonModule, MatDialogModule],
  encapsulation: ViewEncapsulation.None,
  template: `
    <div class="contenido-dialogo">
      <div class="icono-dialogo">{{ getIcono() }}</div>
      <div class="texto-dialogo">
        <h2 class="titulo-dialogo">{{ data.titulo }}</h2>
        <p class="mensaje-dialogo">{{ data.mensaje }}</p>
      </div>
    </div>

    <div mat-dialog-actions align="end" class="acciones-dialogo">
      <button mat-button mat-dialog-close>Cancelar</button>
      <button mat-button [mat-dialog-close]="true">Aceptar</button>
    </div>
  `,
  styles: [
    `
      mat-dialog-container {
        background: linear-gradient(to bottom right, #fdf6e3, #f1d9a3);
        border: 4px solid #8b5e3c;
        border-radius: 14px;
        box-shadow: 0 6px 18px rgba(0, 0, 0, 0.6);
        font-family: 'MedievalSharp', serif;
        padding: 0;
      }

      .contenido-dialogo {
        padding: 2rem;
        display: flex;
        align-items: flex-start;
        gap: 1rem;
        color: #3e2f1c;
      }

      .icono-dialogo {
        font-size: 2.8rem;
      }

      .texto-dialogo h2 {
        font-size: 1.6rem;
        font-weight: bold;
        margin: 0 0 0.4rem 0;
        color: #4a2c0a;
        text-shadow: 1px 1px 1px #e6d2a2;
      }

      .texto-dialogo p {
        font-size: 1.1rem;
        margin: 0;
      }

      .acciones-dialogo {
        display: flex;
        justify-content: flex-end;
        padding: 0 1.5rem 1rem;
      }

      button[mat-button] {
        font-family: 'MedievalSharp', serif;
        background: linear-gradient(to bottom, #f9e6c0, #e3c28b);
        border: 2px solid #8b5e3c;
        border-radius: 8px;
        padding: 0.5rem 1.2rem;
        font-size: 1rem;
        font-weight: bold;
        color: #3e2f1c;
        box-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
        transition: all 0.2s ease;
      }

      button[mat-button]:hover {
        background: linear-gradient(to bottom, #fce9c5, #deb577);
        transform: translateY(-1px);
      }
    `,
  ],
})
export class AlertaDialogComponent {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { titulo: string; mensaje: string }
  ) {}

  getIcono(): string {
    const titulo = this.data.titulo.toLowerCase();
    if (titulo.includes('compra')) return '🛒';
    if (titulo.includes('venta')) return '💰';
    if (titulo.includes('error')) return '❌';
    if (titulo.includes('guardias')) return '🛡️';
    if (titulo.includes('reparar')) return '🔧';
    return '📜';
  }
}
