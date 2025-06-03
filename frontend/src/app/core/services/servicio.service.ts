import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Servicio } from '../models/servicio.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ServicioService {
  private baseUrl = `${environment.apiUrl}/servicios`;

  constructor(private http: HttpClient) {}

  // ✅ GET: listar todos los servicios
  getAll(): Observable<Servicio[]> {
    return this.http.get<Servicio[]>(this.baseUrl);
  }

  // ✅ POST: crear nuevo servicio
  crear(dto: Partial<Servicio>): Observable<Servicio> {
    return this.http.post<Servicio>(this.baseUrl, dto);
  }

  // ✅ PUT: actualizar servicio
  actualizar(id: number, dto: Partial<Servicio>): Observable<Servicio> {
    return this.http.put<Servicio>(`${this.baseUrl}/${id}`, dto);
  }

  // ✅ DELETE: eliminar servicio
  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
  getPorCiudad(ciudadId: number): Observable<Servicio[]> {
    return this.http.get<Servicio[]>(`/api/ciudades/${ciudadId}/servicios`);
  }
}

export type { Servicio };
