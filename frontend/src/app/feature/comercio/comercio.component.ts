import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';
import { AlertaDialogComponent } from '../../dialogs/alerta-dialog.component';

import { MatTabsModule } from '@angular/material/tabs';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { StockService, Stock } from '../../core/services/stock.service';
import { CiudadService } from '../../core/services/ciudad.service';
import { CaravanaService } from '../../core/services/caravana.service';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-comercio',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatTabsModule,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    MatButtonModule,
    MatSnackBarModule,
    MatCardModule,
    MatTableModule,
    MatTooltipModule,
  ],
  templateUrl: './comercio.component.html',
  styleUrls: ['./comercio.component.scss'],
})
export class ComercioComponent implements OnInit {
  ciudadId!: number;
  nombreCiudad = '';
  stocks: Stock[] = [];
  fgComprar!: FormGroup;
  fgVender!: FormGroup;
  dinero = 0;
  cargaActual = 0;
  capacidadMax = 0;
  vida = 0;

  constructor(
    private route: ActivatedRoute,
    private stockSvc: StockService,
    private ciudadSvc: CiudadService,
    private caravanSvc: CaravanaService,
    private fb: FormBuilder,
    private snack: MatSnackBar,
    private dialog: MatDialog
  ) {}
  private cargarEstadoCaravana(): void {
    this.caravanSvc.getEstado().subscribe({
      next: (c) => {
        if (!c) {
          // partida aún no iniciada
          this.snack.open('Aún no has iniciado partida.', 'Cerrar', {
            duration: 3000,
          });
          return;
        }

        this.dinero = c.dinero;
        this.cargaActual = c.cargaActual;
        this.capacidadMax = c.capacidadMax;
        this.vida = c.vida;
      },
      error: () => {
        this.dialog.open(AlertaDialogComponent, {
          data: { titulo: 'Error', mensaje: '❌ Ciudad inválida en la URL' },
        });
      },
    });
  }

  /* comercio.component.ts */

  ngOnInit(): void {
    /* 1️⃣  Tratamos de leer “:id” desde la URL  ----------------------- */
    const idParam = this.route.snapshot.paramMap.get('id');

    if (idParam && !isNaN(+idParam)) {
      /* ✅  Param válido → usamos ese id directamente */
      this.ciudadId = +idParam;
      this.inicializarComercio();
      return;
    }

    /* 2️⃣  Fallback: no vino “:id” → preguntamos a la caravana -------- */
    this.caravanSvc.getEstado().subscribe({
      next: (c) => {
        if (!c) {
          this.snack.open('Primero inicia una partida.', 'Cerrar', {
            duration: 3000,
          });
          return;
        }

        /* Usa el id base de la ciudad si el backend lo envía;
         si no, cae al id del clon CiudadCaravana. */
        this.ciudadId =
          (c as any).ciudadActualBaseId ?? (c as any).ciudadActualId;

        this.inicializarComercio();
      },
      error: () =>
        this.snack.open('No se pudo obtener la ciudad actual', 'Cerrar', {
          duration: 3000,
        }),
    });
  }

  /* ────────────────────────────────────────────────────────────────── */
  /* Helper que reúne toda la lógica que antes estaba en ngOnInit()    */
  private inicializarComercio(): void {
    /* Nombre de la ciudad ------------------------------------------- */
    this.ciudadSvc.getCiudad(this.ciudadId).subscribe({
      next: (c) => (this.nombreCiudad = c.nombre),
      error: () =>
        this.snack.open('No se pudo cargar el nombre de la ciudad', 'Cerrar', {
          duration: 3000,
        }),
    });

    /* Stats de la caravana ------------------------------------------ */
    this.cargarEstadoCaravana();

    /* Formularios ---------------------------------------------------- */
    this.fgComprar = this.fb.group({
      productoId: [null],
      cantidad: [1],
    });

    this.fgVender = this.fb.group({
      productoId: [null],
      cantidad: [1],
    });

    /* Stock de la ciudad -------------------------------------------- */
    this.cargarStock();
  }

  cargarStock() {
    this.stockSvc.getPorCiudad(this.ciudadId).subscribe({
      next: (list) => (this.stocks = list),
      error: () =>
        this.snack.open('No se pudo cargar el stock de la ciudad', 'Cerrar', {
          duration: 3000,
        }),
    });
  }
  getPrecioCompra(productoId: number): number {
    const s = this.stocks.find((p) => p.productoId === productoId);
    const precio = s ? s.fo / (1 + s.cantidad) : 0;
    console.log('getPrecioCompra ->', { productoId, precio, stock: s });
    return +precio.toFixed(2);
  }
  getStockByProductoId(productoId: number): Stock | undefined {
    const result = this.stocks.find((s) => s.productoId === productoId);
    console.log('getStockByProductoId →', { productoId, result });
    return this.stocks.find((s) => s.productoId === productoId);
  }

  getPrecioVenta(productoId: number): number {
    const s = this.stocks.find((p) => p.productoId === productoId);
    return s ? +(s.fd / (1 + s.cantidad)).toFixed(2) : 0;
  }
  onVender() {
    const productoId = +this.fgVender.value.productoId;
    const cantidad = +this.fgVender.value.cantidad;

    if (!productoId || cantidad <= 0) {
      this.dialog.open(AlertaDialogComponent, {
        data: {
          titulo: 'Error de venta',
          mensaje: 'Selecciona un producto y una cantidad válida.',
        },
      });
      return;
    }

    const stock = this.stocks.find((s) => s.productoId === productoId);
    if (!stock) {
      this.dialog.open(AlertaDialogComponent, {
        data: {
          titulo: 'Producto no encontrado',
          mensaje:
            'Este producto no está disponible para vender en esta ciudad.',
        },
      });
      return;
    }

    const precioUnitario = +(stock.fd / (1 + stock.cantidad)).toFixed(2);
    const total = +(precioUnitario * cantidad).toFixed(2);

    console.log('💰 VENTA', { cantidad, precioUnitario, total });

    this.caravanSvc.vender(this.ciudadId, productoId, cantidad).subscribe({
      next: (res) => {
        this.dialog.open(AlertaDialogComponent, {
          data: {
            titulo: 'Venta realizada',
            mensaje: `Vendiste ${cantidad} unidades de ${
              stock.productoNombre
            } por ${total.toFixed(2)} monedas.`,
          },
        });

        this.dinero = res.dinero;
        this.cargaActual = res.cargaActual;
        this.capacidadMax = res.capacidadMax;
        this.vida = res.vida;
        this.cargarStock();
        this.fgVender.reset({ productoId: null, cantidad: 1 });
      },
      error: (e) => {
        const msg = e?.error || e?.message || 'Error desconocido al vender.';
        this.dialog.open(AlertaDialogComponent, {
          data: {
            titulo: 'Error de venta',
            mensaje: msg,
          },
        });
      },
    });
  }

  onComprar() {
    const productoId = +this.fgComprar.value.productoId;
    const cantidad = +this.fgComprar.value.cantidad;

    if (!productoId || cantidad <= 0) {
      this.dialog.open(AlertaDialogComponent, {
        data: {
          titulo: 'Error de compra',
          mensaje: 'Selecciona un producto y una cantidad válida.',
        },
      });
      return;
    }

    const stock = this.stocks.find((s) => s.productoId === productoId);
    if (!stock) {
      this.dialog.open(AlertaDialogComponent, {
        data: {
          titulo: 'Producto no encontrado',
          mensaje: 'Este producto no está disponible en esta ciudad.',
        },
      });
      return;
    }

    const precioUnitario = +(stock.fo / (1 + stock.cantidad)).toFixed(2);
    const total = +(precioUnitario * cantidad).toFixed(2);

    this.caravanSvc.comprar(this.ciudadId, productoId, cantidad).subscribe({
      next: (res) => {
        this.dialog.open(AlertaDialogComponent, {
          data: {
            titulo: 'Compra realizada',
            mensaje: `Compraste ${cantidad} unidades de ${
              stock.productoNombre
            } por ${total.toFixed(2)} monedas.`,
          },
        });

        this.dinero = res.dinero;
        this.cargaActual = res.cargaActual;
        this.capacidadMax = res.capacidadMax;
        this.vida = res.vida;
        this.cargarStock();
        this.fgComprar.reset({ productoId: null, cantidad: 1 });
      },
      error: (e) => {
        const msg = e?.error || e?.message || 'Error desconocido al comprar.';
        this.dialog.open(AlertaDialogComponent, {
          data: {
            titulo: 'Error de compra',
            mensaje: msg,
          },
        });
      },
    });
  }
}
