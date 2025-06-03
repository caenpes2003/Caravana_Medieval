import { Component, OnInit, OnDestroy, ViewEncapsulation } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { RouterModule } from '@angular/router';
import { interval, Subscription } from 'rxjs';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { CaravanaService } from '../../core/services/caravana.service';
import { Caravana } from '../../core/models/caravana.model';
import { EndGameDialogComponent } from '../../dialogs/endgame-dialog.component';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

import { Stock } from '../../core/models/stock.model';
import { JugadorConectado } from '../../core/models/jugador-conectado.model';

@Component({
  selector: 'app-caravana',
  standalone: true,
  encapsulation: ViewEncapsulation.None,
  imports: [
    CommonModule,
    RouterModule,
    MatSnackBarModule,
    MatDialogModule,
    MatButtonModule,
    MatProgressBarModule,
    MatIconModule,
    MatTooltipModule,
  ],
  templateUrl: './caravana.component.html',
  styleUrls: ['./caravana.component.scss'],
})
export class CaravanaComponent implements OnInit, OnDestroy {
  caravana?: Caravana;
  comercioEnabled = false;
  travelEnabled = false;
  progresoEconomico = 0;
  juegoTerminado = false;
  private refreshSub?: Subscription;
  private dialogAbierto = false;
  formattedTiempo = '';
  private syncSub?: Subscription;
  private clockSub?: Subscription;
  private jugadoresSub?: Subscription;
  serviceEnable = false;

  constructor(
    private svc: CaravanaService,
    private snack: MatSnackBar,
    private dialog: MatDialog,
    private router: Router,
    public auth: AuthService
  ) {}

  jugadoresConectados: JugadorConectado[] = JSON.parse(
    sessionStorage.getItem('jugadores') ?? '[]'
  );

  ngOnInit() {
    this.loadEstado();
    this.syncSub = interval(1000).subscribe(() => this.loadEstado());

    if (this.auth.esAdministrador()) {
      this.cargarJugadoresConectados();
      this.jugadoresSub = interval(1_000).subscribe(() =>
        this.cargarJugadoresConectados()
      );
    }
  }

  ngOnDestroy() {
    this.syncSub?.unsubscribe();
    this.jugadoresSub?.unsubscribe();
  }

  private updateTiempo() {
    if (
      !this.caravana ||
      !this.caravana.ultimaActualizacion ||
      this.juegoTerminado
    )
      return;

    const inicio = new Date(this.caravana.ultimaActualizacion).getTime();
    const ahora = Date.now();
    const segundosTranscurridos = Math.floor((ahora - inicio) / 1000);
    let total = this.caravana.tiempoTranscurrido + segundosTranscurridos;

    if (total >= this.caravana.tiempoLimite || this.caravana.vida <= 0) {
      total = this.caravana.tiempoLimite;
      this.caravana.tiempoMostrado = total;
      this.juegoTerminado = true;
      this.refreshSub?.unsubscribe();

      if (!this.dialogAbierto) {
        this.dialogAbierto = true;
        this.dialog
          .open(EndGameDialogComponent, {
            data: { mensaje: 'Se acabó el tiempo o la vida. ¡Game Over!' },
          })
          .afterClosed()
          .subscribe(() => {
            this.dialogAbierto = false;
          });
      }
      return;
    }
    this.caravana.tiempoMostrado = total;
    this.formattedTiempo = this.formatTiempo(total);
  }

  mostrarInventario = false;
  stockCaravana: Stock[] = [];

  toggleInventario() {
    this.mostrarInventario = !this.mostrarInventario;
    if (this.mostrarInventario) {
      this.svc.getInventario().subscribe({
        next: (stock) => (this.stockCaravana = stock),
        error: (err) => {
          console.error('❌ Error al cargar inventario', err);
          this.snack.open('No se pudo cargar el inventario', 'Cerrar', {
            duration: 3000,
          });
        },
      });
    }
  }

  private loadEstado(): void {
    this.svc.getEstado().subscribe({
      /* ──────────────────────────────────────────────────────────────
       1. Respuesta OK
       ────────────────────────────────────────────────────────────── */
      next: (c) => {
        /* 1A. Partida todavía no iniciada (backend retornó 204 → null) */
        if (c === null) {
          this.snack.open('Aún no has iniciado partida.', 'Cerrar', {
            duration: 3000,
          });
          this.caravana = undefined;
          this.comercioEnabled = false;
          this.travelEnabled = false;
          this.progresoEconomico = 0;
          this.serviceEnable = false;
          return;
        }

        /* 1B. Caravana válida */
        this.caravana = c;
        console.log('🛡️ Estado de la caravana:', this.caravana);

        /* Rutas y banderas */
        this.caravana.rutasDisponibles ??= [];
        this.travelEnabled =
          !this.auth.esAdministrador() &&
          (this.caravana.rutasDisponibles?.length ?? 0) > 0 && !this.auth.esComerciante();
        this.comercioEnabled =
          this.caravana.haViajado && !this.auth.esAdministrador() ;
          this.serviceEnable = !this.auth.esComerciante();
          this.serviceEnable = this.auth.esCaravanero();

        /* Economía */
        this.calcularProgresoEconomico();
      },

      /* ──────────────────────────────────────────────────────────────
       2. Error
       ────────────────────────────────────────────────────────────── */
      error: (err) => {
        /* 2A. Juego terminado – backend lanza 422 "Game Over" */
        if (err.status === 422 && (err.error as string).includes('Game Over')) {
          this.juegoTerminado = true;
          if (!this.dialogAbierto) {
            this.dialogAbierto = true;
            this.dialog
              .open(EndGameDialogComponent, {
                data: { mensaje: err.error },
              })
              .afterClosed()
              .subscribe(() => (this.dialogAbierto = false));
          }
          return;
        }

        /* 2B. Cualquier otro error */
        this.snack.open('No se pudo cargar el estado', 'Cerrar', {
          duration: 3000,
        });
        console.error('❌ Error /api/caravana →', err);
      },
    });
  }

  calcularProgresoEconomico() {
    if (!this.caravana) return;
    const actual = this.caravana.dinero;
    const inicial = this.caravana.dineroInicial;
    const meta = this.caravana.metaGanancia;
    const progreso = Math.max(0, ((actual - inicial) * 100) / (meta - inicial));
    this.progresoEconomico = Math.min(100, progreso);
  }

  getEconomiaTexto(): string {
    if (!this.caravana) return '';
    const actual = this.caravana.dinero;
    const meta = this.caravana.metaGanancia;
    return `💰 Progreso: ${actual} / ${meta} monedas`;
  }

  getEstaPerdiendo(): boolean {
    return (
      !!this.caravana && this.caravana.dinero < this.caravana.dineroInicial
    );
  }
  irARutas() {
    this.router.navigate(['/rutas']);
  }
  irAComercio(): void {
    if (!this.caravana) {
      this.snack.open('Caravana no cargada', 'Cerrar', { duration: 2500 });
      return;
    }

    const ciudadId =
      (this.caravana as any).ciudadActualBaseId ?? this.caravana.ciudadActualId;

    this.router.navigate(['/comercio', ciudadId]);
  }

  irAServicios() {
    this.router.navigate(['/servicios']);
  }

  reiniciar() {
    console.log('🔄 Reiniciando partida...');
    localStorage.removeItem('historialRutas');
    this.svc.reset().subscribe({
      next: () => this.router.navigate(['/']),
      //next: () => location.reload(),
      error: (err) => {
        this.snack.open('Error al reiniciar partida', 'Cerrar', {
          duration: 3000,
        });
        console.error('❌ Error al reiniciar partida:', err);
      },
    });
  }
  getTiempoTexto(): string {
    if (!this.caravana) return '';
    const total =
      this.caravana.tiempoMostrado ?? this.caravana.tiempoTranscurrido;
    const lim = this.caravana.tiempoLimite;
    return `⏳ Tiempo: ${this.formatTiempo(total)} / ${this.formatTiempo(lim)}`;
  }
  private formatTiempo(segs: number): string {
    const h = Math.floor(segs / 3600);
    const m = Math.floor((segs % 3600) / 60);
    const s = segs % 60;
    return `${h}h ${m}m ${s}s`;
  }

  private cargarJugadoresConectados(): void {
    this.svc.getJugadoresConectados().subscribe({
      next: (jugadores) => (this.jugadoresConectados = jugadores),
      error: (err) =>
        console.error('❌ No se pudieron cargar los jugadores', err),
    });
  }
}
