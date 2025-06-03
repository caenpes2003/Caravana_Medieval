import { Component, Inject }                  from '@angular/core';
import { MatDialogModule,
         MatDialogRef,
         MAT_DIALOG_DATA }                    from '@angular/material/dialog';
import { MatButtonModule }                    from '@angular/material/button';
import { MatSnackBarModule, MatSnackBar }     from '@angular/material/snack-bar';
import { CaravanaService }                    from '../core/services/caravana.service';
import { Router }                             from '@angular/router';
import { CommonModule }                       from '@angular/common';

@Component({
  selector: 'app-endgame-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    MatSnackBarModule
  ],
  template: `
    <h2 mat-dialog-title>Fin de la partida</h2>
    <mat-dialog-content>{{ data.mensaje }}</mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-raised-button color="primary" (click)="reiniciar()">🔄 Reiniciar partida</button>
      <button mat-button (click)="dialogRef.close()">Cerrar</button>
    </mat-dialog-actions>
  `
})
export class EndGameDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<EndGameDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { mensaje: string },
    private carSvc: CaravanaService,
    private snack: MatSnackBar,
    private router: Router
  ) {}

  reiniciar() {
    localStorage.removeItem('historialRutas');

    this.carSvc.reset().subscribe({
      next: () => {
        this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
          this.router.navigate(['/']);
          this.dialogRef.close();
          
          this.snack.open('✅ Partida reiniciada', 'Cerrar', { duration: 3000 });
        });
      },
      error: err => {
        console.error('❌ Error al reiniciar partida:', err);
        this.snack.open('No se pudo reiniciar la partida', 'Cerrar', { duration: 3000 });
        this.dialogRef.close();
      }
    });
  }
}
