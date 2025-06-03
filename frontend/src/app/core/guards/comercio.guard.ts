import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { MatDialog } from '@angular/material/dialog';
import { AccesoDenegadoDialogComponent } from '../../dialogs/acceso-denegado-dialog/acceso-denegado-dialog.component';
@Injectable({ providedIn: 'root' })
export class ComercioGuard implements CanActivate {
  constructor(
    private auth: AuthService,
    private router: Router,
    private dialog: MatDialog
  ) {}

  canActivate(): boolean | UrlTree{
    if (this.auth.esCaravanero() || this.auth.esComerciante()) {
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
