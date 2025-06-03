import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Ruta } from '../models/ruta.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class RutaService {
  private baseUrl = `${environment.apiUrl}/rutas`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Ruta[]> {
    return this.http.get<Ruta[]>(`${environment.apiUrl}/rutas`);
  }
}