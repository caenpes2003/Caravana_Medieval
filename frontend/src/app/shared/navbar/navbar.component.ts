import { Component, computed, ViewEncapsulation } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';
import { CaravanaService } from '../../core/services/caravana.service';

@Component({
  selector   : 'app-navbar',
  standalone : true,
  imports    : [
    MatToolbarModule, MatButtonModule, MatIconModule,
    MatMenuModule,    MatDividerModule,
    CommonModule,     RouterModule
  ],
  templateUrl : './navbar.component.html',
  styleUrls   : ['./navbar.component.scss'],
  //encapsulation: ViewEncapsulation.None
})
export class NavbarComponent {


  constructor(
    private caravanSvc : CaravanaService,
    public  auth       : AuthService,
    private router     : Router
  ) {}

  get esAdmin()       { return this.auth.getUsuario()?.rol === 'ADMINISTRADOR'; }
  get esCaravanero()  { return this.auth.getUsuario()?.rol === 'CARAVANERO'; }
  get esComerciante() { return this.auth.getUsuario()?.rol === 'COMERCIANTE'; }

  /** Reinicia la partida y la sesión */
  onReset(): void {
    localStorage.clear();

    this.caravanSvc.reset().subscribe({
      next: () => this.auth.logout().subscribe({
        next  : () => location.href = '/',
        error : () => location.href = '/'
      }),
      error: err => console.error('Error al reiniciar partida', err)
    });
  }

  cerrarSesion(): void {
    this.auth.logout().subscribe(() => this.router.navigate(['/login']));
  }
}
