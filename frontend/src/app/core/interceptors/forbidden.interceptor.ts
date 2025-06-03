import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpInterceptor,
  HttpHandler,
  HttpRequest,
  HttpErrorResponse,
} from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { AccesoDenegadoDialogComponent } from '../../dialogs/acceso-denegado-dialog/acceso-denegado-dialog.component';

@Injectable()
export class ForbiddenInterceptor implements HttpInterceptor {
  constructor(private dialog: MatDialog, private router: Router) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((err: HttpErrorResponse) => {
        if (err.status === 403) {
          this.dialog.open(AccesoDenegadoDialogComponent, {
            width: '320px',
            disableClose: true,
          });
        }

        return throwError(() => err);
      })
    );
  }
}
