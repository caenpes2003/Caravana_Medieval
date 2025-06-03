import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { MatDialog } from '@angular/material/dialog';
import { AccesoDenegadoDialogComponent } from '../../dialogs/acceso-denegado-dialog/acceso-denegado-dialog.component';
@Injectable({ providedIn: 'root' })
export class ServiciosGuard implements CanActivate {
  constructor(
    private auth: AuthService,
    private router: Router,
    private dialog: MatDialog
  ) {}

  canActivate(): boolean | UrlTree {
    /* ✅ 1. Si es admin → permitir */
    if (this.auth.esCaravanero()) {
      return true;
    }
    this.dialog.open(AccesoDenegadoDialogComponent, {
      width: '320px',
      disableClose: true,
    });
    /* ❌ 3. Redirigir a la página de inicio */
    return this.router.createUrlTree(['/caravana']);
  }
}
