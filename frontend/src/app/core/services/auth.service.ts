import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, tap } from 'rxjs/operators';
import { Observable, of } from 'rxjs';

export interface AuthResponse {
  id: number;
  nombre: string;
  rol: 'ADMINISTRADOR' | 'CARAVANERO' | 'COMERCIANTE';
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  /** Usuario en memoria (vive mientras la SPA no recargue). */
  private usuarioActual?: AuthResponse;

  constructor(private http: HttpClient) {
    /* 🔄  Restauramos, si existe, el usuario guardado durante una visita anterior */
    const guardado = sessionStorage.getItem('usuario');
    if (guardado) {
      this.usuarioActual = JSON.parse(guardado);
    }
  }

  /* ──────────────────────────── autenticación ───────────────────────────── */

  login(nombre: string, password: string): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(
        '/api/auth/login',
        { nombre, password },
        { withCredentials: true }
      )
      .pipe(
        tap((res) => {
          this.usuarioActual = res;
          sessionStorage.setItem('usuario', JSON.stringify(res));
        })
      );
  }

  logout(): Observable<void> {
    return this.http
      .post<void>('/api/auth/logout', {}, { withCredentials: true })
      .pipe(
        tap(() => {
          this.usuarioActual = undefined;
          sessionStorage.removeItem('usuario');
        })
      );
  }

  registrar(data: {
    nombre: string;
    password: string;
    rol: string;
    nombreCaravana?: string;
    caravanaId?: number;
  }): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>('/api/auth/registrar', data, {
        withCredentials: true,
      })
      .pipe(
        tap((res) => {
          this.usuarioActual = res;
          sessionStorage.setItem('usuario', JSON.stringify(res));
        })
      );
  }

  /** Devuelve el usuario según la cookie de sesión (o `null` si no hay). */
  me(): Observable<AuthResponse | null> {
    return this.http
      .get<AuthResponse>('/api/auth/me', { withCredentials: true })
      .pipe(
        tap((res) => {
          this.usuarioActual = res;
          sessionStorage.setItem('usuario', JSON.stringify(res));
        }),
        catchError(() => {
          this.usuarioActual = undefined;
          sessionStorage.removeItem('usuario');
          return of(null);
        })
      );
  }

  /* ──────────────────────────── helpers varios ──────────────────────────── */

  obtenerCaravanasDisponibles(): Observable<{ id: number; nombre: string }[]> {
    return this.http.get<{ id: number; nombre: string }[]>(
      '/api/caravana/disponibles'
    );
  }

  getUsuario(): AuthResponse | undefined {
    return this.usuarioActual;
  }

  estaAutenticado(): boolean {
    return !!this.usuarioActual;
  }

  esAdministrador(): boolean {
    return this.usuarioActual?.rol === 'ADMINISTRADOR';
  }

  esCaravanero(): boolean {
    return this.usuarioActual?.rol === 'CARAVANERO';
  }

  esComerciante(): boolean {
    return this.usuarioActual?.rol === 'COMERCIANTE';
  }
}
